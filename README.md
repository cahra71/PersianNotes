PersianNotes - Full sample project (Light Samsung Notes-like)
============================================================

This repository contains a runnable Android project (Kotlin) with:
- Handwriting canvas (DrawingView)
- ML Kit Digital Ink Recognition integration (Farsi model)
- Room database for storing notes metadata
- Basic export to PNG and PDF from the canvas
- GitHub Actions workflow to build APK automatically

Important:
- Add your ketabi.ttf to app/src/main/res/font/ketabi.ttf
- For building on GitHub Actions, network access to google maven must be available.
- To get APK: push this project to a GitHub repo and the workflow will produce an artifact.
