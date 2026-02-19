# Eclipse Open VSX

Open VSX is an open-source VS Code extension marketplace. It provides a registry for VS Code extensions, a web UI for browsing/installing them, and a CLI for publishing.

## Repository Structure

```
server/   — Java/Spring Boot backend (REST API, storage, search, jobs)
webui/    — React/TypeScript frontend
cli/      — Node.js CLI for publishing extensions (ovsx)
deploy/   — Kubernetes deployment configs
doc/      — Documentation
scripts/  — Helper scripts
```

See `server/CLAUDE.md` for server-specific details.

## Local Development with Docker Compose

The `docker-compose.yml` at the repo root provides services via profiles:

```bash
docker compose --profile db up          # PostgreSQL only
docker compose --profile es up          # Elasticsearch only
docker compose --profile db --profile es up  # Both (typical for server dev)
docker compose --profile redis up       # Redis cluster (6 nodes)
docker compose --profile openvsx up     # Full stack (db + es + server + webui + cli)
docker compose --profile backend up     # Server only (with db + es)
docker compose --profile frontend up    # Web UI only (depends on server)
docker compose --profile debug up       # db + es (alias for local debugging)
```

Default credentials: `openvsx` / `openvsx` (PostgreSQL).

## Contributing

- Sign the [Eclipse Contributor Agreement](https://www.eclipse.org/legal/ECA.php) before submitting PRs.
- Non-committers must include `Signed-off-by` in commit messages (`git commit -s`).
- See `CONTRIBUTING.md` for full details.
