# 🤖 Spring AI & Ollama Local Chat Application

[![Java](https://img.shields.io/badge/Java-25%2B-orange?style=for-the-badge&logo=java)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.7-brightgreen?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Spring AI](https://img.shields.io/badge/Spring%20AI-Ollama-blue?style=for-the-badge&logo=spring)](https://spring.io/projects/spring-ai)
[![Ollama](https://img.shields.io/badge/Ollama-Llama%203.2latest-black?style=for-the-badge&logo=ollama)](https://ollama.com/)

A modern, production-ready Spring Boot microservice demonstrating integration with local Large Language Models (LLMs) using **Spring AI** and **Ollama**.

This project showcases how to build privacy-focused, cost-effective AI applications in Java without relying on third-party cloud APIs (like OpenAI or Anthropic).

---

## 🌟 Key Features

* **Privacy & Local Execution:** Runs completely offline using Ollama—no data leaves the local machine.
* **Cost Efficiency:** Zero API usage costs or token management overhead.
* **Spring AI Integration:** Uses Spring AI's high-level `ChatClient` fluent API abstraction for clean, maintainable prompt construction.
* **REST API Endpoint:** Simple, scalable endpoint for synchronous chat interactions.
* **Configurable Model Options:** Dynamically adjust temperature, default models, and connection properties via standard Spring configuration files.

---

## 🛠️ Tech Stack

* **Language:** Java 25+
* **Framework:** Spring Boot 4.0.x
* **AI Abstraction Layer:** Spring AI (`spring-ai-ollama-spring-boot-starter`)
* **LLM Engine:** Ollama (`llama3.2` model)
* **Build Tool:** Maven / Gradle
* **Version Control:** Git & GitHub

---

## 🏗️ Architecture Flow