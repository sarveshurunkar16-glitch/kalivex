from passlib.context import CryptContext

pwd_ctx = CryptContext(schemes=["bcrypt"], deprecated="auto")

def hash_credential(raw: str) -> str:
    return pwd_ctx.hash(raw)

def verify_credential(raw: str, hashed: str) -> bool:
    try:
        return pwd_ctx.verify(raw, hashed)
    except Exception:
        return False
