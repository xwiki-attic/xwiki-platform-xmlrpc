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
package org.xwiki.test.xmlrpc.confluence;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xwiki.test.integration.XWikiTestSetup;
import org.xwiki.test.xmlrpc.*;
import org.xwiki.test.xmlrpc.AttachmentsTest;
import org.xwiki.test.xmlrpc.PagesTest;
import org.xwiki.test.xmlrpc.PagesTestWithGuest;
import org.xwiki.test.xmlrpc.SpacesTest;

/**
 * A class listing all the XMLRPC Functional tests to execute. We need such a class (rather than
 * letting the JUnit Runner discover the different TestCases classes by itself) because we want to
 * start/stop XWiki before and after the tests start (but only once).
 *
 * @version $Id: ee825269653389c901a174b31238e4499b338ff2 $
 */
public class AllTests extends TestCase
{
    private static final String PATTERN = ".*" + System.getProperty("pattern", "");

    public static Test suite() throws Exception
    {
        TestSuite suite = new TestSuite();

        // TODO: Refactor this to use XWikiExecutorSuite instead
        addTestCase(suite, AnonymousAccessTest.class);
        addTestCase(suite, AttachmentsTest.class);
        addTestCase(suite, CommentsTest.class);
        addTestCase(suite, PagesTest.class);
        addTestCase(suite, PagesTestWithGuest.class);
        addTestCase(suite, SpacesTest.class);

        addTestCase(suite, AttachmentsTest.class);
        addTestCase(suite, AuthenticationTest.class);
        addTestCase(suite, org.xwiki.test.xmlrpc.CommentsTest.class);
        addTestCase(suite, PagesTest.class);
        addTestCase(suite, SpacesTest.class);
        addTestCase(suite, XWikiClassesTest.class);
        addTestCase(suite, XWikiObjectsTest.class);
        addTestCase(suite, SearchTest.class);
        addTestCase(suite, RenderingTest.class);

        return new XWikiTestSetup(suite);
    }

    private static void addTestCase(TestSuite suite, Class testClass) throws Exception
    {
        if (testClass.getName().matches(PATTERN)) {
            suite.addTest(new TestSuite(testClass));
        }
    }

    private static void addTest(TestSuite suite, Test test, Class testClass) throws Exception
    {
        if (testClass.getName().matches(PATTERN)) {
            suite.addTest(test);
        }
    }
}
