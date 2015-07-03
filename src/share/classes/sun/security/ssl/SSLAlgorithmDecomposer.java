/*
 * Copyright (c) 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package sun.security.ssl;

import java.util.HashSet;
import java.util.Set;
import sun.security.util.AlgorithmDecomposer;
import static sun.security.ssl.CipherSuite.*;
import static sun.security.ssl.CipherSuite.KeyExchange.*;

/**
 * The class decomposes standard SSL/TLS cipher suites into sub-elements.
 */
class SSLAlgorithmDecomposer extends AlgorithmDecomposer {

    // indicates that only certification path algorithms need to be used
    private final boolean onlyX509;

    SSLAlgorithmDecomposer(boolean onlyX509) {
        this.onlyX509 = onlyX509;
    }

    SSLAlgorithmDecomposer() {
        this(false);
    }

    private Set<String> decomposes(CipherSuite.KeyExchange keyExchange) {
        Set<String> components = new HashSet<>();
        switch (keyExchange) {
            case K_NULL:
                if (!onlyX509) {
                    components.add("K_NULL");
                }
                break;
            case K_RSA:
                components.add("RSA");
                break;
            case K_RSA_EXPORT:
                components.add("RSA");
                components.add("RSA_EXPORT");
                break;
            case K_DH_RSA:
                components.add("RSA");
                components.add("DH");
                components.add("DiffieHellman");
                components.add("DH_RSA");
                break;
            case K_DH_DSS:
                components.add("DSA");
                components.add("DSS");
                components.add("DH");
                components.add("DiffieHellman");
                components.add("DH_DSS");
                break;
            case K_DHE_DSS:
                components.add("DSA");
                components.add("DSS");
                components.add("DH");
                components.add("DHE");
                components.add("DiffieHellman");
                components.add("DHE_DSS");
                break;
            case K_DHE_RSA:
                components.add("RSA");
                components.add("DH");
                components.add("DHE");
                components.add("DiffieHellman");
                components.add("DHE_RSA");
                break;
            case K_DH_ANON:
                if (!onlyX509) {
                    components.add("ANON");
                    components.add("DH");
                    components.add("DiffieHellman");
                    components.add("DH_ANON");
                }
                break;
            case K_ECDH_ECDSA:
                components.add("ECDH");
                components.add("ECDSA");
                components.add("ECDH_ECDSA");
                break;
            case K_ECDH_RSA:
                components.add("ECDH");
                components.add("RSA");
                components.add("ECDH_RSA");
                break;
            case K_ECDHE_ECDSA:
                components.add("ECDHE");
                components.add("ECDSA");
                components.add("ECDHE_ECDSA");
                break;
            case K_ECDHE_RSA:
                components.add("ECDHE");
                components.add("RSA");
                components.add("ECDHE_RSA");
                break;
            case K_ECDH_ANON:
                if (!onlyX509) {
                    components.add("ECDH");
                    components.add("ANON");
                    components.add("ECDH_ANON");
                }
                break;
            case K_KRB5:
                if (!onlyX509) {
                    components.add("KRB5");
                }
                break;
            case K_KRB5_EXPORT:
                if (!onlyX509) {
                    components.add("KRB5_EXPORT");
                }
                break;
            default:
                // ignore
            }

        return components;
    }

    private Set<String> decomposes(CipherSuite.BulkCipher bulkCipher) {
        Set<String> components = new HashSet<>();

        if (bulkCipher.transformation != null) {
            components.addAll(super.decompose(bulkCipher.transformation));
        }

        if (bulkCipher == B_NULL) {
            components.add("C_NULL");
        } else if (bulkCipher == B_RC2_40) {
            components.add("RC2_CBC_40");
        } else if (bulkCipher == B_RC4_40) {
            components.add("RC4_40");
        } else if (bulkCipher == B_RC4_128) {
            components.add("RC4_128");
        } else if (bulkCipher == B_DES_40) {
            components.add("DES40_CBC");
            components.add("DES_CBC_40");
        } else if (bulkCipher == B_DES) {
            components.add("DES_CBC");
        } else if (bulkCipher == B_3DES) {
            components.add("3DES_EDE_CBC");
        } else if (bulkCipher == B_AES_128) {
            components.add("AES_128_CBC");
        } else if (bulkCipher == B_AES_256) {
            components.add("AES_256_CBC");
        }

        return components;
    }

    private Set<String> decomposes(CipherSuite.MacAlg macAlg,
            BulkCipher cipher) {
        Set<String> components = new HashSet<>();

        if (macAlg == M_NULL) {
            components.add("M_NULL");
        } else if (macAlg == M_MD5) {
            components.add("MD5");
            components.add("HmacMD5");
        } else if (macAlg == M_SHA) {
            components.add("SHA1");
            components.add("SHA-1");
            components.add("HmacSHA1");
        } else if (macAlg == M_SHA256) {
            components.add("SHA256");
            components.add("SHA-256");
            components.add("HmacSHA256");
        } else if (macAlg == M_SHA384) {
            components.add("SHA384");
            components.add("SHA-384");
            components.add("HmacSHA384");
        }

        return components;
    }

    private Set<String> decompose(KeyExchange keyExchange, BulkCipher cipher,
            MacAlg macAlg) {
        Set<String> components = new HashSet<>();

        if (keyExchange != null) {
            components.addAll(decomposes(keyExchange));
        }

        if (onlyX509) {
            // Certification path algorithm constraints do not apply
            // to cipher and macAlg.
            return components;
        }

        if (cipher != null) {
            components.addAll(decomposes(cipher));
        }

        if (macAlg != null) {
            components.addAll(decomposes(macAlg, cipher));
        }

        return components;
    }

    @Override
    public Set<String> decompose(String algorithm) {
        if (algorithm.startsWith("SSL_") || algorithm.startsWith("TLS_")) {
            CipherSuite cipherSuite = null;
            try {
                cipherSuite = CipherSuite.valueOf(algorithm);
            } catch (IllegalArgumentException iae) {
                // ignore: unknown or unsupported ciphersuite
            }

            if (cipherSuite != null) {
                return decompose(cipherSuite.keyExchange, cipherSuite.cipher,
                        cipherSuite.macAlg);
            }
        }

        return super.decompose(algorithm);
    }

}
