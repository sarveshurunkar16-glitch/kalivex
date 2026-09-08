from sqlalchemy import Column, Integer, String, DateTime, func, JSON
from app.database import Base

class CommandQueue(Base):
    __tablename__ = "command_queue"
    id = Column(Integer, primary_key=True, index=True)
    user_id = Column(Integer, nullable=False, index=True)
    device_id = Column(Integer, nullable=True, index=True)
    name = Column(String(128), nullable=False)
    payload = Column(JSON, nullable=True)
    status = Column(String(32), default="pending", nullable=False)  # pending/sent/acknowledged/failed
    requires_confirmation = Column(Integer, default=0)  # 0 false, 1 true
    created_at = Column(DateTime(timezone=True), server_default=func.now())
    sent_at = Column(DateTime(timezone=True), nullable=True)
    acknowledged_at = Column(DateTime(timezone=True), nullable=True)
