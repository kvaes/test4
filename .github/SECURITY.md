# Security Policy

## Supported Versions

We release security updates for the following versions:

| Version | Supported          |
| ------- | ------------------ |
| 1.x     | :white_check_mark: |
| < 1.0   | :x:                |

## Reporting a Vulnerability

We take the security of the BICS Semantic Kernel Agent seriously. If you believe you have found a security vulnerability, please report it to us as described below.

### How to Report a Security Vulnerability

**Please do not report security vulnerabilities through public GitHub issues.**

Instead, please report them via email to: security@bics.com

You should receive a response within 48 hours. If for some reason you do not, please follow up via email to ensure we received your original message.

Please include the following information when reporting a vulnerability:

- Type of issue (e.g. buffer overflow, SQL injection, cross-site scripting, etc.)
- Full paths of source file(s) related to the manifestation of the issue
- The location of the affected source code (tag/branch/commit or direct URL)
- Any special configuration required to reproduce the issue
- Step-by-step instructions to reproduce the issue
- Proof-of-concept or exploit code (if possible)
- Impact of the issue, including how an attacker might exploit the issue

This information will help us triage your report more quickly.

### What to Expect

After you submit a report, we will:

1. Acknowledge receipt of your vulnerability report within 48 hours
2. Provide an estimated timeframe for addressing the vulnerability
3. Notify you when the vulnerability is fixed
4. Credit you in our security advisory (if desired)

### Preferred Languages

We prefer all communications to be in English.

## Security Update Process

When we receive a security bug report, we will:

1. Confirm the problem and determine the affected versions
2. Audit code to find any potential similar problems
3. Prepare fixes for all releases still under maintenance
4. Release new versions as soon as possible
5. Publish a security advisory on GitHub

## Scope

This security policy applies to:

- The BICS Semantic Kernel Agent codebase
- All dependencies and third-party components
- Infrastructure and deployment configurations
- API endpoints and data handling

### Out of Scope

The following are generally not considered security vulnerabilities:

- Issues in third-party applications or services not directly controlled by this project
- Issues that require physical access to infrastructure
- Issues that require social engineering
- Reports from automated scanning tools without verification

## Additional Security Measures

### Code Security

- All code changes are reviewed by at least one other developer
- Dependencies are regularly updated and scanned for vulnerabilities
- Static analysis security testing (SAST) is performed on all commits
- Dynamic analysis security testing (DAST) is performed on deployed applications

### Infrastructure Security

- All communication is encrypted in transit using TLS 1.3
- Authentication and authorization are required for all API endpoints
- Infrastructure follows the principle of least privilege
- Regular security assessments are conducted

### Data Protection

- All personal data is handled according to applicable privacy laws
- Data encryption is used for data at rest and in transit
- Access to sensitive data is logged and monitored
- Data retention policies are implemented and followed

## Contact

For any questions about this security policy, please contact: security@bics.com

## Recognition

We appreciate the efforts of security researchers and will acknowledge their contributions in our security advisories (with their permission).

Thank you for helping keep the BICS Semantic Kernel Agent and our users safe!