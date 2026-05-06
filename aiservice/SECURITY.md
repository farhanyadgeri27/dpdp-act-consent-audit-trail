# SECURITY.md

## Security Risks
- Prompt injection
- API abuse
- Invalid requests
- AI service downtime

## Security Protections
- Flask rate limiting enabled
- Prompt injection detection implemented
- Environment variables used for API secrets
- Retry handling for API failures

## Security Testing
- Tested empty input handling
- Tested invalid request handling
- Tested prompt injection patterns
- Tested API response failures

## Residual Risks
- AI hallucinations may occur
- External API downtime possible

## Notes
No sensitive credentials are hardcoded in source files.