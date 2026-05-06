from flask import Flask, request, jsonify

from flask_limiter import Limiter
from flask_limiter.util import get_remote_address

from services.groq_client import generate_response

app = Flask(__name__)

limiter = Limiter(
    get_remote_address,
    app=app,
    default_limits=["30 per minute"]
)

def detect_injection(text):

    blocked_words = [
        "ignore previous instructions",
        "<script>",
        "drop table",
        "--"
    ]

    text = text.lower()

    return any(word in text for word in blocked_words)

@app.route("/health")
def health():

    return jsonify({
        "status": "healthy",
        "model": "llama-3.3-70b-versatile"
    })

@app.route("/generate-report", methods=["POST"])
@limiter.limit("30 per minute")
def generate_report():

    data = request.json

    text = data.get("text", "")

    if not text:

        return jsonify({
            "error": "Input required"
        }), 400

    if detect_injection(text):

        return jsonify({
            "error": "Prompt injection detected"
        }), 400

    prompt = f"""
    Create a professional consent audit report.

    Input:
    {text}

    Return:
    - Title
    - Summary
    - Recommendations
    """

    result = generate_response(prompt)

    return jsonify(result)

if __name__ == "__main__":

    app.run(
        host="0.0.0.0",
        port=5000
    )