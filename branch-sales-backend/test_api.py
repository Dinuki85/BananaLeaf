import urllib.request
import urllib.error
import sys

try:
    with urllib.request.urlopen("http://localhost:8080/api/products") as response:
        print(response.read().decode('utf-8'))
except urllib.error.HTTPError as e:
    print(f"HTTP Error: {e.code}")
    print(e.read().decode('utf-8'))
except Exception as e:
    print(f"Other Error: {e}")
