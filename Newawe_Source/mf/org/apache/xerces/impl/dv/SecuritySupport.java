package mf.org.apache.xerces.impl.dv;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

final class SecuritySupport {

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.1 */
    class C08751 implements PrivilegedAction {
        C08751() {
        }

        public Object run() {
            ClassLoader cl = null;
            try {
                cl = Thread.currentThread().getContextClassLoader();
            } catch (SecurityException e) {
            }
            return cl;
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.2 */
    class C08762 implements PrivilegedAction {
        C08762() {
        }

        public Object run() {
            ClassLoader cl = null;
            try {
                cl = ClassLoader.getSystemClassLoader();
            } catch (SecurityException e) {
            }
            return cl;
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.3 */
    class C08773 implements PrivilegedAction {
        private final /* synthetic */ ClassLoader val$cl;

        C08773(ClassLoader classLoader) {
            this.val$cl = classLoader;
        }

        public Object run() {
            ClassLoader parent = null;
            try {
                parent = this.val$cl.getParent();
            } catch (SecurityException e) {
            }
            return parent == this.val$cl ? null : parent;
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.4 */
    class C08784 implements PrivilegedAction {
        private final /* synthetic */ String val$propName;

        C08784(String str) {
            this.val$propName = str;
        }

        public Object run() {
            return System.getProperty(this.val$propName);
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.5 */
    class C08795 implements PrivilegedExceptionAction {
        private final /* synthetic */ File val$file;

        C08795(File file) {
            this.val$file = file;
        }

        public Object run() throws FileNotFoundException {
            return new FileInputStream(this.val$file);
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.6 */
    class C08806 implements PrivilegedAction {
        private final /* synthetic */ ClassLoader val$cl;
        private final /* synthetic */ String val$name;

        C08806(ClassLoader classLoader, String str) {
            this.val$cl = classLoader;
            this.val$name = str;
        }

        public Object run() {
            if (this.val$cl == null) {
                return ClassLoader.getSystemResourceAsStream(this.val$name);
            }
            return this.val$cl.getResourceAsStream(this.val$name);
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.7 */
    class C08817 implements PrivilegedAction {
        private final /* synthetic */ File val$f;

        C08817(File file) {
            this.val$f = file;
        }

        public Object run() {
            return this.val$f.exists() ? Boolean.TRUE : Boolean.FALSE;
        }
    }

    /* renamed from: mf.org.apache.xerces.impl.dv.SecuritySupport.8 */
    class C08828 implements PrivilegedAction {
        private final /* synthetic */ File val$f;

        C08828(File file) {
            this.val$f = file;
        }

        public Object run() {
            return new Long(this.val$f.lastModified());
        }
    }

    static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new C08751());
    }

    static ClassLoader getSystemClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new C08762());
    }

    static ClassLoader getParentClassLoader(ClassLoader cl) {
        return (ClassLoader) AccessController.doPrivileged(new C08773(cl));
    }

    static String getSystemProperty(String propName) {
        return (String) AccessController.doPrivileged(new C08784(propName));
    }

    static FileInputStream getFileInputStream(File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new C08795(file));
        } catch (PrivilegedActionException e) {
            throw ((FileNotFoundException) e.getException());
        }
    }

    static InputStream getResourceAsStream(ClassLoader cl, String name) {
        return (InputStream) AccessController.doPrivileged(new C08806(cl, name));
    }

    static boolean getFileExists(File f) {
        return ((Boolean) AccessController.doPrivileged(new C08817(f))).booleanValue();
    }

    static long getLastModified(File f) {
        return ((Long) AccessController.doPrivileged(new C08828(f))).longValue();
    }

    private SecuritySupport() {
    }
}
