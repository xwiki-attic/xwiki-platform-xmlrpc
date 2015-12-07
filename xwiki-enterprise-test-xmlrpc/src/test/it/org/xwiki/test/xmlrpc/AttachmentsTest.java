/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.test.xmlrpc;

import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.codehaus.swizzle.confluence.Attachment;
import org.xwiki.xmlrpc.model.Utils;
import org.xwiki.xmlrpc.model.XWikiPage;

/**
 * @version $Id: dfcd317423910420f292124ae8a1bb0fd8055ada $
 */
public class AttachmentsTest extends AbstractXWikiXmlRpcTest
{
    public void setUp() throws Exception
    {
        super.setUp();

        try {
            rpc.removePage(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        } catch (XmlRpcException e) {
            // Page doesn't exist.
        }

        XWikiPage page = new XWikiPage();
        page.setId(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        rpc.storePage(page);

        // Make sure the test page has at least one attachment.
        addAttachment("first.txt", "Content of first attachment.");
    }

    private Attachment addAttachment(String fileName, String content) throws Exception
    {
        Attachment attachment = new Attachment();
        attachment.setPageId(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        attachment.setFileName(fileName);
        return rpc.addAttachment(0, attachment, content.getBytes());
    }

    public void testAddAttachment() throws Exception
    {
        String attachmentName = String.format("test_attachment_%d.png", random.nextInt());
        String attachmentData = "Data for " + attachmentName;
        Attachment attachment = addAttachment(attachmentName, "Data for " + attachmentName);

        TestUtils.banner("TEST: addAttachment()");
        System.out.format("%s\n", attachment);

        // We can't assert the file name because it can be transformed (e.g. some characters could be removed).
        assertEquals(attachmentData.getBytes().length, Integer.parseInt(attachment.getFileSize()));

        // Let's see if the attachment was properly added.
        assertEquals(attachmentData,
            new String(rpc.getAttachmentData(attachment.getPageId(), attachment.getFileName(), "1.1")));
    }

    public void testGetAttachments() throws Exception
    {
        List<Attachment> attachments = rpc.getAttachments(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);

        TestUtils.banner("TEST: getAttachments()");
        for (Attachment attachment : attachments) {
            System.out.format("%s\n", attachment);
        }

        assertFalse(attachments.isEmpty());
    }

    public void testGetAttachmentData() throws Exception
    {
        List<Attachment> attachments = rpc.getAttachments(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        Attachment attachment = attachments.get(0);

        byte[] content = rpc.getAttachmentData(attachment.getPageId(), attachment.getFileName(), "1.1");

        TestUtils.banner("getAttachmentData()");
        System.out.format("%s\n", attachment);
        System.out.format("Content = %s\n",
            Utils.truncateToFirstLine(new String(content, 0, content.length > 32 ? 32 : content.length)));

        int contentLength = new Integer(attachment.getFileSize());
        assertTrue(content.length == contentLength);
    }

    public void testRemoveAttachment() throws Exception
    {
        List<Attachment> attachments = rpc.getAttachments(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        Attachment attachmentToBeRemoved = attachments.get(random.nextInt(attachments.size()));

        TestUtils.banner("TEST: testRemoveAttachment()");
        System.out.format("Before: %s\n", attachments);
        Boolean result = rpc.removeAttachment(attachmentToBeRemoved.getPageId(), attachmentToBeRemoved.getFileName());

        System.out.format("Result: %b\n", result);

        attachments = rpc.getAttachments(TestConstants.TEST_PAGE_WITH_ATTACHMENTS);
        System.out.format("After: %s\n", attachments);
        boolean found = false;
        for (Attachment attachment : attachments) {
            if (attachment.getFileName().equals(attachmentToBeRemoved.getFileName())) {
                found = true;
                break;
            }
        }

        assertFalse(found);
    }
}
