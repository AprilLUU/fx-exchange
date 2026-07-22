# fx-monitor

A lightweight dashboard that watches the fx-app-spring API.

## What it shows
- Live exchange rates (all pairs, latest date)
- API health status (green = up, red = down)
- Poll counter (increments every 5 seconds)
- ACCEPTING toggle (requires `/api/admin/accepting` — built in Task 5)

## How it works
nginx serves the static page **and** proxies every `/api/` and `/health` call
to the upstream Spring app. The browser never talks to the API directly.

## Environment variables
| Variable   | Default        | Purpose                         |
|------------|----------------|---------------------------------|
| `API_HOST` | `fx-app-spring`| Upstream service name or host   |

## Running standalone (useless — for structure inspection only)
```bash
docker build -t fx-monitor:1.0 .
docker run -p 3000:80 fx-monitor:1.0
# Page loads but all calls fail — no compose network, API_HOST unresolvable
```

## Running in the stack
See `docker-compose.yml` at the workspace root.
