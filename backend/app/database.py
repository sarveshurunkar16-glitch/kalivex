from sqlalchemy.ext.asyncio import create_async_engine, AsyncSession
from sqlalchemy.orm import sessionmaker, declarative_base
from .config import settings

DATABASE_URL = settings.DATABASE_URL
engine = create_async_engine(DATABASE_URL, future=True, echo=False)
AsyncSessionLocal = sessionmaker(engine, expire_on_commit=False, class_=AsyncSession)
Base = declarative_base()

async def init_db():
    # Use Alembic in production. For dev-only convenience create the schema if env allows.
    create_schema = True if os.getenv("DEV_CREATE_SCHEMA", "true").lower() in ("1","true","yes") else False
    if create_schema:
        async with engine.begin() as conn:
            await conn.run_sync(Base.metadata.create_all)
