import os
import time

from groq import Groq
from dotenv import load_dotenv

load_dotenv()

client = Groq(
    api_key=os.getenv("GROQ_API_KEY")
)

def generate_response(prompt):

    retries = 3

    for attempt in range(retries):

        try:

            response = client.chat.completions.create(

                model="llama-3.3-70b-versatile",

                messages=[
                    {
                        "role": "system",
                        "content": "You are an audit assistant."
                    },
                    {
                        "role": "user",
                        "content": prompt
                    }
                ],

                temperature=0.3,
                max_tokens=500
            )

            return {
                "success": True,
                "response": response.choices[0].message.content,
                "is_fallback": False
            }

        except Exception as e:

            print(f"Error: {e}")

            time.sleep(2)

    return {
        "success": False,
        "response": "AI service unavailable",
        "is_fallback": True
    }