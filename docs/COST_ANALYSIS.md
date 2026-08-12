# Project Echo - Cost Analysis & Optimization
**Date:** August 2026

## 1. Infrastructure Baseline (Monthly)
- **Application Servers (Backend):** $150 (2x c6g.large)
- **Frontend CDN (Vercel/Cloudflare):** $20
- **PostgreSQL Database:** $200 (Managed Multi-AZ r6g.large)
- **Redis Cache:** $50 (Managed cache.t4g.small)
- **Object Storage (Evidence):** $40 (1TB Standard Storage)
- **Observability (Grafana Cloud/Datadog):** $50 (Free tier + overages)
**Total Infrastructure:** ~$510 / month

## 2. AI Cost Engineering (GPT-4o/Claude 3.5 Sonnet)
- **Assessment AI Prompt Tokens:** ~2,000 per assessment
- **Assessment AI Completion Tokens:** ~1,000 per assessment
- **Cost per Assessment:** ~$0.02
- **Estimated Assessments/Month:** 5,000
**Total AI Cost:** ~$100 / month

## 3. Total Projected Monthly Burn
- **Base Total:** ~$610 / month for up to 10,000 active users.

## 4. Optimization Opportunities
- Enable semantic caching in the AI Gateway to reduce token costs by ~30%.
- Utilize ARM64 (Graviton) instances for 20% cost reduction on compute.
- Implement Object Storage Lifecycle rules to move >30 days old evidence to Infrequent Access storage.
