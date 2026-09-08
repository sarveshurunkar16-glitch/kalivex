from fastapi import APIRouter
from app.api.ws import websocket_endpoint

router = APIRouter()

# WS router inclusion is handled in app.main; this file exists to keep routing consistent.
