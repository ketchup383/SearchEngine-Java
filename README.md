# Mini Search Engine
A simplified search engine implemented entirely from scratch in Java, designed to demonstrate core computer science concepts such as hash tables, graph traversal, sorting algorithms, and page ranking.

This project simulates how real search engines (like Google) crawl, index, and rank web pages — but using a small, local XML-based “internet”.

## Technologies Used
**Language**: Java \
**Core Concepts**: Data Structures, Algorithms, Graph Theory, Sorting, Hashing
**Parsing**: XML

## Features
**Custom Hash Table** – Implemented an efficient key–value data structure to index web pages and their content.

**Graph Construction** (WebGraph) – Modeled a web of connected pages using adjacency lists.

**PageRank Algorithm** – Computed page importance scores using iterative rank propagation.

**Search Functionality** – Returned relevant pages ranked by PageRank score.

**Efficient Sorting** – Implemented a fastSort() algorithm (O(n log n)) using Merge Sort for result ordering.

**Crawling Simulation** – Parsed XML “web” files to mimic real-world link structures.