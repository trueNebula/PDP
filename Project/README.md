**Team members:** Daniel Gulei-Gradinaru, Alexia Goia
 
## Goal
 
The goal of this project is to implement and compare two approaches for graph coloring. The N-coloring problem involves assigning colors to the vertices of a graph in such a way that no two adjacent vertices share the same color, using a maximum of N colors.
 
## Requirement
 
Each project will have 2 implementations: one with "regular" threads or tasks/futures, and one distributed (possibly, but not required, using MPI).
 
## Computer Specification
 
- **CPU:** AMD Ryzen 7 5800HS with Radeon Graphics, 3201 Mhz, 8 Core(s), 16 Logical Processor(s)
- **RAM:** 16 GB
 
### Method
 
The chosen method for graph coloring is a recursive algorithm that explores possible colorings of the graph vertices and backtracks when invalid color assignments are encountered.
 
## Short Description of the Implementation:
 
### Threads
 
In the thread-based implementation, a thread pool of size 10 is utilized. Each thread is responsible for processing a portion of the graph, executing the graph coloring algorithm concurrently. The threads collaborate to explore possible colorings, and the final result is determined by aggregating their findings. At the end, the program waits for all threads to complete their tasks.
 
### Distributed - MPI
 
In the distributed implementation using MPI, the graph coloring problem is divided among different processes. Each process explores a portion of the solution space, and communication between processes is established through MPI messages. The solution is then obtained by combining the results from different processes.
 
## Performance Tests
 
Performance tests were conducted on graphs of varying sizes to evaluate the efficiency of both implementations. In 10% of the tests run, MPI would run slower than threads, but we did a computation to ensure accuracy of results.
 
| Algorithm | Graph Size: 50<br>Colors: 7 | Graph Size: 10<br>Colors: 5 | Graph Size: 100<br>Colors: 10 | Graph Size: 500<br>Colors: 10 |
| ---- | ---- | ---- | ---- | ---- |
| Threads | 18 ms | 2 ms | 54 ms | 260 ms |
| Distributed MPI | 3 ms | 1 ms | 9 ms | 76 ms |

As data exponentially grows, so does the time for the threaded approach, while MPI is better at keeping the pace.
## Results
 
The results of the performance tests demonstrate the efficiency of both thread-based and distributed MPI implementations for graph coloring. The times reported in the table represent the execution time for solving graph coloring problems of different sizes.
 
## Conclusion
 
The project successfully implemented and compared two approaches for graph coloring: a thread-based implementation and a distributed MPI implementation. The performance tests provided insights into the efficiency of each approach for solving graph coloring problems of varying complexities. 