import sys
import os

sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..')))

from app import app

client = app.test_client()

def test_health():

    response = client.get("/health")

    assert response.status_code == 200

def test_empty_input():

    response = client.post(
        "/generate-report",
        json={"text": ""}
    )

    assert response.status_code == 400