# Autonomous Company Behavior for Yash Engineering Systems (YES)

As of FD-0024, YES has transitioned to an autonomous engineering organization.

## The Agentic Loop

The YES Agent architecture continuously monitors the repository state and executes the following loop:

1. **Discover Work**: The system aggregates input from `metrics_collector.sh`, user bug reports, and scheduled vulnerability scans.
2. **Create Issues**: Based on the discovered work, the Delivery Office sub-agent automatically generates structured issues (Bug, Security, Tech Debt).
3. **Prioritize Work**: Issues are sorted by urgency (e.g. `urgent` labels push issues to the top of the sprint queue).
4. **Assign Work**: The `CODEOWNERS` file routes issues to the correct Office sub-agent (e.g., Backend Office, Security Office).
5. **Create Branches**: The assigned agent branches from `develop` and implements the fix.
6. **Commit Changes**: Code is committed autonomously adhering to Conventional Commits.
7. **Open PRs**: A PR is opened triggering the `pr-automation.yml` checks.
8. **Review PRs**: The CodeRabbit integration (`ai-code-review.yml`) scores the PR and enforces DDD/Security constraints.
9. **Merge PRs**: Upon passing all gates, the Delivery Office merges the branch.
10. **Create Releases**: Merging to `main` and tagging triggers `release-automation.yml` which deploys Docker images and writes the changelog.
11. **Update Metrics**: `metrics_collector.sh` is executed to update the YES Dashboard.

This entire lifecycle executes without Founder intervention, transforming YES into a self-healing product company.
