package mf.org.apache.xerces.impl.dv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import mf.org.apache.xerces.impl.xs.SchemaSymbols;

final class ObjectFactory {
    private static final boolean DEBUG;
    private static final int DEFAULT_LINE_LENGTH = 80;
    private static final String DEFAULT_PROPERTIES_FILENAME = "xerces.properties";
    private static long fLastModified;
    private static Properties fXercesProperties;

    static final class ConfigurationError extends Error {
        static final long serialVersionUID = 8521878292694272124L;
        private Exception exception;

        ConfigurationError(String msg, Exception x) {
            super(msg);
            this.exception = x;
        }

        Exception getException() {
            return this.exception;
        }
    }

    ObjectFactory() {
    }

    static {
        DEBUG = isDebugEnabled();
        fXercesProperties = null;
        fLastModified = -1;
    }

    static Object createObject(String factoryId, String fallbackClassName) throws ConfigurationError {
        return createObject(factoryId, null, fallbackClassName);
    }

    static Object createObject(String factoryId, String propertiesFilename, String fallbackClassName) throws ConfigurationError {
        boolean loadProperties;
        long j;
        long lastModified;
        Object provider;
        if (DEBUG) {
            debugPrintln("debug is on");
        }
        ClassLoader cl = findClassLoader();
        try {
            String systemProp = SecuritySupport.getSystemProperty(factoryId);
            if (systemProp != null && systemProp.length() > 0) {
                if (DEBUG) {
                    debugPrintln("found system property, value=" + systemProp);
                }
                return newInstance(systemProp, cl, true);
            }
        } catch (SecurityException e) {
        }
        String factoryClassName = null;
        FileInputStream fis;
        if (propertiesFilename == null) {
            File propertiesFile = null;
            boolean propertiesFileExists = DEBUG;
            try {
                propertiesFilename = new StringBuilder(String.valueOf(SecuritySupport.getSystemProperty("java.home"))).append(File.separator).append("lib").append(File.separator).append(DEFAULT_PROPERTIES_FILENAME).toString();
                File propertiesFile2 = new File(propertiesFilename);
                try {
                    propertiesFileExists = SecuritySupport.getFileExists(propertiesFile2);
                    propertiesFile = propertiesFile2;
                } catch (SecurityException e2) {
                    propertiesFile = propertiesFile2;
                    fLastModified = -1;
                    fXercesProperties = null;
                    synchronized (ObjectFactory.class) {
                        loadProperties = DEBUG;
                        fis = null;
                        try {
                            if (fLastModified < 0) {
                                if (propertiesFileExists) {
                                    j = fLastModified;
                                    lastModified = SecuritySupport.getLastModified(propertiesFile);
                                    fLastModified = lastModified;
                                    if (j < lastModified) {
                                        loadProperties = true;
                                    }
                                }
                                if (!propertiesFileExists) {
                                    fLastModified = -1;
                                    fXercesProperties = null;
                                }
                            } else if (propertiesFileExists) {
                                loadProperties = true;
                                fLastModified = SecuritySupport.getLastModified(propertiesFile);
                            }
                            if (loadProperties) {
                                fXercesProperties = new Properties();
                                fis = SecuritySupport.getFileInputStream(propertiesFile);
                                fXercesProperties.load(fis);
                            }
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e3) {
                                }
                            }
                        } catch (Exception e4) {
                            fXercesProperties = null;
                            fLastModified = -1;
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e5) {
                                }
                            }
                        } catch (Throwable th) {
                            if (fis != null) {
                                try {
                                    fis.close();
                                } catch (IOException e6) {
                                }
                            }
                        }
                    }
                    if (fXercesProperties != null) {
                        factoryClassName = fXercesProperties.getProperty(factoryId);
                    }
                    if (factoryClassName != null) {
                        provider = findJarServiceProvider(factoryId);
                        if (provider == null) {
                            return provider;
                        }
                        if (fallbackClassName == null) {
                            if (DEBUG) {
                                debugPrintln("using fallback, value=" + fallbackClassName);
                            }
                            return newInstance(fallbackClassName, cl, true);
                        }
                        throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
                    }
                    if (DEBUG) {
                        debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
                    }
                    return newInstance(factoryClassName, cl, true);
                }
            } catch (SecurityException e7) {
                fLastModified = -1;
                fXercesProperties = null;
                synchronized (ObjectFactory.class) {
                    loadProperties = DEBUG;
                    fis = null;
                    if (fLastModified < 0) {
                        if (propertiesFileExists) {
                            j = fLastModified;
                            lastModified = SecuritySupport.getLastModified(propertiesFile);
                            fLastModified = lastModified;
                            if (j < lastModified) {
                                loadProperties = true;
                            }
                        }
                        if (propertiesFileExists) {
                            fLastModified = -1;
                            fXercesProperties = null;
                        }
                    } else if (propertiesFileExists) {
                        loadProperties = true;
                        fLastModified = SecuritySupport.getLastModified(propertiesFile);
                    }
                    if (loadProperties) {
                        fXercesProperties = new Properties();
                        fis = SecuritySupport.getFileInputStream(propertiesFile);
                        fXercesProperties.load(fis);
                    }
                    if (fis != null) {
                        fis.close();
                    }
                }
                if (fXercesProperties != null) {
                    factoryClassName = fXercesProperties.getProperty(factoryId);
                }
                if (factoryClassName != null) {
                    if (DEBUG) {
                        debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
                    }
                    return newInstance(factoryClassName, cl, true);
                }
                provider = findJarServiceProvider(factoryId);
                if (provider == null) {
                    return provider;
                }
                if (fallbackClassName == null) {
                    throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
                }
                if (DEBUG) {
                    debugPrintln("using fallback, value=" + fallbackClassName);
                }
                return newInstance(fallbackClassName, cl, true);
            }
            synchronized (ObjectFactory.class) {
                loadProperties = DEBUG;
                fis = null;
                if (fLastModified < 0) {
                    if (propertiesFileExists) {
                        j = fLastModified;
                        lastModified = SecuritySupport.getLastModified(propertiesFile);
                        fLastModified = lastModified;
                        if (j < lastModified) {
                            loadProperties = true;
                        }
                    }
                    if (propertiesFileExists) {
                        fLastModified = -1;
                        fXercesProperties = null;
                    }
                } else if (propertiesFileExists) {
                    loadProperties = true;
                    fLastModified = SecuritySupport.getLastModified(propertiesFile);
                }
                if (loadProperties) {
                    fXercesProperties = new Properties();
                    fis = SecuritySupport.getFileInputStream(propertiesFile);
                    fXercesProperties.load(fis);
                }
                if (fis != null) {
                    fis.close();
                }
            }
            if (fXercesProperties != null) {
                factoryClassName = fXercesProperties.getProperty(factoryId);
            }
        } else {
            fis = null;
            try {
                fis = SecuritySupport.getFileInputStream(new File(propertiesFilename));
                Properties props = new Properties();
                props.load(fis);
                factoryClassName = props.getProperty(factoryId);
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e8) {
                    }
                }
            } catch (Exception e9) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e10) {
                    }
                }
            } catch (Throwable th2) {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e11) {
                    }
                }
            }
        }
        if (factoryClassName != null) {
            if (DEBUG) {
                debugPrintln("found in " + propertiesFilename + ", value=" + factoryClassName);
            }
            return newInstance(factoryClassName, cl, true);
        }
        provider = findJarServiceProvider(factoryId);
        if (provider == null) {
            return provider;
        }
        if (fallbackClassName == null) {
            throw new ConfigurationError("Provider for " + factoryId + " cannot be found", null);
        }
        if (DEBUG) {
            debugPrintln("using fallback, value=" + fallbackClassName);
        }
        return newInstance(fallbackClassName, cl, true);
    }

    private static boolean isDebugEnabled() {
        try {
            String val = SecuritySupport.getSystemProperty("xerces.debug");
            if (val == null || SchemaSymbols.ATTVAL_FALSE.equals(val)) {
                return DEBUG;
            }
            return true;
        } catch (SecurityException e) {
            return DEBUG;
        }
    }

    private static void debugPrintln(String msg) {
        if (DEBUG) {
            System.err.println("XERCES: " + msg);
        }
    }

    static ClassLoader findClassLoader() throws ConfigurationError {
        ClassLoader chain;
        ClassLoader context = SecuritySupport.getContextClassLoader();
        ClassLoader system = SecuritySupport.getSystemClassLoader();
        for (chain = system; context != chain; chain = SecuritySupport.getParentClassLoader(chain)) {
            if (chain == null) {
                return context;
            }
        }
        ClassLoader current = ObjectFactory.class.getClassLoader();
        for (chain = system; current != chain; chain = SecuritySupport.getParentClassLoader(chain)) {
            if (chain == null) {
                return current;
            }
        }
        return system;
    }

    static Object newInstance(String className, ClassLoader cl, boolean doFallback) throws ConfigurationError {
        try {
            Class providerClass = findProviderClass(className, cl, doFallback);
            Object instance = providerClass.newInstance();
            if (DEBUG) {
                debugPrintln("created new instance of " + providerClass + " using ClassLoader: " + cl);
            }
            return instance;
        } catch (ClassNotFoundException x) {
            throw new ConfigurationError("Provider " + className + " not found", x);
        } catch (Exception x2) {
            throw new ConfigurationError("Provider " + className + " could not be instantiated: " + x2, x2);
        }
    }

    static Class findProviderClass(String className, ClassLoader cl, boolean doFallback) throws ClassNotFoundException, ConfigurationError {
        SecurityManager security = System.getSecurityManager();
        if (security != null) {
            int lastDot = className.lastIndexOf(".");
            String packageName = className;
            if (lastDot != -1) {
                packageName = className.substring(0, lastDot);
            }
            security.checkPackageAccess(packageName);
        }
        if (cl == null) {
            return Class.forName(className);
        }
        try {
            return cl.loadClass(className);
        } catch (ClassNotFoundException x) {
            if (doFallback) {
                ClassLoader current = ObjectFactory.class.getClassLoader();
                if (current == null) {
                    return Class.forName(className);
                }
                if (cl != current) {
                    return current.loadClass(className);
                }
                throw x;
            }
            throw x;
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.lang.Object findJarServiceProvider(java.lang.String r12) throws mf.org.apache.xerces.impl.dv.ObjectFactory.ConfigurationError {
        /*
        r11 = 80;
        r8 = 0;
        r9 = new java.lang.StringBuilder;
        r10 = "META-INF/services/";
        r9.<init>(r10);
        r9 = r9.append(r12);
        r6 = r9.toString();
        r4 = 0;
        r0 = findClassLoader();
        r4 = mf.org.apache.xerces.impl.dv.SecuritySupport.getResourceAsStream(r0, r6);
        if (r4 != 0) goto L_0x002a;
    L_0x001d:
        r9 = mf.org.apache.xerces.impl.dv.ObjectFactory.class;
        r1 = r9.getClassLoader();
        if (r0 == r1) goto L_0x002a;
    L_0x0025:
        r0 = r1;
        r4 = mf.org.apache.xerces.impl.dv.SecuritySupport.getResourceAsStream(r0, r6);
    L_0x002a:
        if (r4 != 0) goto L_0x002d;
    L_0x002c:
        return r8;
    L_0x002d:
        r9 = DEBUG;
        if (r9 == 0) goto L_0x004d;
    L_0x0031:
        r9 = new java.lang.StringBuilder;
        r10 = "found jar resource=";
        r9.<init>(r10);
        r9 = r9.append(r6);
        r10 = " using ClassLoader: ";
        r9 = r9.append(r10);
        r9 = r9.append(r0);
        r9 = r9.toString();
        debugPrintln(r9);
    L_0x004d:
        r5 = new java.io.BufferedReader;	 Catch:{ UnsupportedEncodingException -> 0x0089 }
        r9 = new java.io.InputStreamReader;	 Catch:{ UnsupportedEncodingException -> 0x0089 }
        r10 = "UTF-8";
        r9.<init>(r4, r10);	 Catch:{ UnsupportedEncodingException -> 0x0089 }
        r10 = 80;
        r5.<init>(r9, r10);	 Catch:{ UnsupportedEncodingException -> 0x0089 }
    L_0x005b:
        r3 = 0;
        r3 = r5.readLine();	 Catch:{ IOException -> 0x0095, all -> 0x009c }
        r5.close();	 Catch:{ IOException -> 0x00a3 }
    L_0x0063:
        if (r3 == 0) goto L_0x002c;
    L_0x0065:
        r9 = "";
        r9 = r9.equals(r3);
        if (r9 != 0) goto L_0x002c;
    L_0x006d:
        r8 = DEBUG;
        if (r8 == 0) goto L_0x0083;
    L_0x0071:
        r8 = new java.lang.StringBuilder;
        r9 = "found in resource, value=";
        r8.<init>(r9);
        r8 = r8.append(r3);
        r8 = r8.toString();
        debugPrintln(r8);
    L_0x0083:
        r8 = 0;
        r8 = newInstance(r3, r0, r8);
        goto L_0x002c;
    L_0x0089:
        r2 = move-exception;
        r5 = new java.io.BufferedReader;
        r9 = new java.io.InputStreamReader;
        r9.<init>(r4);
        r5.<init>(r9, r11);
        goto L_0x005b;
    L_0x0095:
        r7 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x009a }
        goto L_0x002c;
    L_0x009a:
        r9 = move-exception;
        goto L_0x002c;
    L_0x009c:
        r8 = move-exception;
        r5.close();	 Catch:{ IOException -> 0x00a1 }
    L_0x00a0:
        throw r8;
    L_0x00a1:
        r9 = move-exception;
        goto L_0x00a0;
    L_0x00a3:
        r9 = move-exception;
        goto L_0x0063;
        */
        throw new UnsupportedOperationException("Method not decompiled: mf.org.apache.xerces.impl.dv.ObjectFactory.findJarServiceProvider(java.lang.String):java.lang.Object");
    }
}
