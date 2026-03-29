package com.library.util;

import com.library.model.Resource;
import com.library.model.Resource.ResourceType;
import com.library.model.Resource.Status;
import com.library.service.LibraryService;

/**
 * Populates the library with 20 realistic technical resources covering
 * machine learning, distributed systems, programming languages, and more.
 *
 * Call this only when the library is empty (first run).
 */
public class SampleDataLoader {

    public static void load(LibraryService service) {
        Object[][] data = {
            // title, author, type, category, url, tags, status, rating, notes
            {
                "Attention Is All You Need",
                "Vaswani et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1706.03762",
                "transformers,nlp,attention",
                Status.COMPLETED, 5,
                "Foundational paper for modern LLMs. The multi-head attention mechanism is key."
            },
            {
                "MapReduce: Simplified Data Processing on Large Clusters",
                "Dean & Ghemawat",
                ResourceType.RESEARCH_PAPER,
                "Distributed Systems",
                "https://research.google/pubs/pub62/",
                "mapreduce,google,big-data",
                Status.COMPLETED, 4,
                "Sparked the big-data era. Good read before studying Hadoop."
            },
            {
                "BERT: Pre-training of Deep Bidirectional Transformers",
                "Devlin et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1810.04805",
                "bert,nlp,transformers",
                Status.COMPLETED, 5,
                "Builds on the Transformer architecture. Compare with GPT approach."
            },
            {
                "The Go Programming Language",
                "Donovan & Kernighan",
                ResourceType.BOOK,
                "Programming Languages",
                "https://www.gopl.io/",
                "golang,concurrency,systems",
                Status.READING, 4,
                "Excellent writing style. Goroutines chapter is very practical."
            },
            {
                "Dynamo: Amazon's Highly Available Key-value Store",
                "DeCandia et al.",
                ResourceType.RESEARCH_PAPER,
                "Distributed Systems",
                "https://dl.acm.org/doi/10.1145/1294261.1294281",
                "dynamo,eventual-consistency,nosql",
                Status.COMPLETED, 5,
                "Must-read for understanding eventual consistency and vector clocks."
            },
            {
                "Playing Atari with Deep Reinforcement Learning",
                "Mnih et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1312.5602",
                "reinforcement-learning,dqn,deepmind",
                Status.UNREAD, 0,
                ""
            },
            {
                "Designing Data-Intensive Applications",
                "Martin Kleppmann",
                ResourceType.BOOK,
                "Distributed Systems",
                "https://dataintensive.net/",
                "databases,distributed,reliability",
                Status.READING, 5,
                "Best book on distributed systems for practitioners. Chapter 5 on replication is gold."
            },
            {
                "Spanner: Google's Globally Distributed Database",
                "Corbett et al.",
                ResourceType.RESEARCH_PAPER,
                "Distributed Systems",
                "https://research.google/pubs/pub39966/",
                "spanner,sql,distributed,google",
                Status.COMPLETED, 4,
                "TrueTime API is fascinating. Helped understand linearizability."
            },
            {
                "Java Concurrency in Practice",
                "Goetz et al.",
                ResourceType.BOOK,
                "Java",
                "https://jcip.net/",
                "java,concurrency,threads,synchronization",
                Status.COMPLETED, 5,
                "Essential for every Java developer. Executor framework explanation is excellent."
            },
            {
                "Clean Code",
                "Robert C. Martin",
                ResourceType.BOOK,
                "Software Engineering",
                "https://www.oreilly.com/library/view/clean-code/9780136083238/",
                "clean-code,refactoring,best-practices",
                Status.COMPLETED, 4,
                "Some opinions are dated but naming conventions and function size advice is timeless."
            },
            {
                "The Anatomy of a Large-Scale Hypertextual Web Search Engine",
                "Brin & Page",
                ResourceType.RESEARCH_PAPER,
                "Algorithms",
                "https://research.google/pubs/pub334/",
                "pagerank,search,google,graph",
                Status.COMPLETED, 5,
                "Original PageRank paper. The graph-based ranking idea changed the internet."
            },
            {
                "Neural Ordinary Differential Equations",
                "Chen et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1806.07366",
                "neural-ode,continuous-models,deep-learning",
                Status.UNREAD, 0,
                "Recommended by professor. Need to refresh ODE knowledge first."
            },
            {
                "Introduction to Algorithms (CLRS)",
                "Cormen, Leiserson, Rivest, Stein",
                ResourceType.BOOK,
                "Algorithms",
                "https://mitpress.mit.edu/books/introduction-algorithms-fourth-edition",
                "algorithms,data-structures,complexity",
                Status.READING, 5,
                "Reference book. Currently on Chapter 22 – Graph Algorithms."
            },
            {
                "A Few Useful Things to Know About Machine Learning",
                "Pedro Domingos",
                ResourceType.ARTICLE,
                "Machine Learning",
                "https://dl.acm.org/doi/10.1145/2347736.2347755",
                "ml,overfitting,bias-variance",
                Status.COMPLETED, 4,
                "Short but dense. The 12 lessons are a great mental model checklist."
            },
            {
                "Raft: In Search of an Understandable Consensus Algorithm",
                "Ongaro & Ousterhout",
                ResourceType.RESEARCH_PAPER,
                "Distributed Systems",
                "https://raft.github.io/raft.pdf",
                "raft,consensus,distributed,leader-election",
                Status.COMPLETED, 5,
                "Much clearer than Paxos. Implemented a basic version after reading."
            },
            {
                "Deep Residual Learning for Image Recognition",
                "He et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1512.03385",
                "resnet,cnn,computer-vision,skip-connections",
                Status.COMPLETED, 4,
                "Skip connections were the breakthrough. Enabled very deep networks."
            },
            {
                "Effective Java",
                "Joshua Bloch",
                ResourceType.BOOK,
                "Java",
                "https://www.oreilly.com/library/view/effective-java/9780134686097/",
                "java,best-practices,api-design",
                Status.COMPLETED, 5,
                "Item 17 on minimising mutability and Item 64 on interfaces are top picks."
            },
            {
                "The Byzantine Generals Problem",
                "Lamport, Shostak, Pease",
                ResourceType.RESEARCH_PAPER,
                "Distributed Systems",
                "https://dl.acm.org/doi/10.1145/357172.357176",
                "byzantine-fault,consensus,fault-tolerance",
                Status.COMPLETED, 4,
                "Classic. Needed to understand this before tackling blockchain consensus."
            },
            {
                "Generative Adversarial Nets",
                "Goodfellow et al.",
                ResourceType.RESEARCH_PAPER,
                "Machine Learning",
                "https://arxiv.org/abs/1406.2661",
                "gans,generative-models,deep-learning",
                Status.COMPLETED, 5,
                "Brilliant idea. Min-max game framing of training is elegant."
            },
            {
                "The Art of Multiprocessor Programming",
                "Herlihy & Shavit",
                ResourceType.BOOK,
                "Java",
                "https://www.elsevier.com/books/the-art-of-multiprocessor-programming/herlihy/978-0-12-370591-4",
                "concurrency,lock-free,java,multiprocessor",
                Status.UNREAD, 0,
                "On reading list after Java Concurrency in Practice."
            }
        };

        for (Object[] row : data) {
            Resource r = new Resource(
                    (String) row[0],
                    (String) row[1],
                    (ResourceType) row[2],
                    (String) row[3],
                    (String) row[4]
            );
            for (String tag : ((String) row[5]).split(",")) {
                r.addTag(tag.trim());
            }
            r.setStatus((Status) row[6]);
            r.setRating((int) row[7]);
            if (!((String) row[8]).isBlank()) {
                r.setNotes((String) row[8]);
            }
            service.addResource(r);
        }

        System.out.println("  [OK] 20 sample resources loaded.");
    }
}