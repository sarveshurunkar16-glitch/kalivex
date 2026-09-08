from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from loguru import logger
import os

from .api import auth, chat, commands, health, devices, ws as ws_router
from .database import init_db


def create_app() -> FastAPI:
    app = FastAPI(title="Kalivex API", version="0.1.0")

    origins = os.getenv("CORS_ORIGINS", "")
    allow_origins = [o.strip() for o in origins.split(",") if o.strip()]

    app.add_middleware(
        CORSMiddleware,
        allow_origins=allow_origins or ["*"],
        allow_credentials=True,
        allow_methods=["*"],
        allow_headers=["*"],
    )

    app.include_router(health.router, prefix="/api")
    app.include_router(auth.router, prefix="/api/auth", tags=["auth"])
    app.include_router(chat.router, prefix="/api", tags=["chat"])
    app.include_router(commands.router, prefix="/api/commands", tags=["commands"])
    app.include_router(devices.router, prefix="/api/devices", tags=["devices"])
    # websocket route will be mounted separately in ASGI; include its router for import consistency

    return app


app = create_app()


@app.on_event("startup")
async def on_start():
    logger.info("Starting Kalivex backend")
    await init_db()
