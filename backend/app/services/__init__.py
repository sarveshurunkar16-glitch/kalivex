from .ai_provider import get_ai_provider
from .command_service import validate_command, route_command_to_device
from .chat_service import create_or_get_conversation, save_message
from .user_service import get_user_by_id

# Instantiate the active ai provider for the application
ai_provider = get_ai_provider()
