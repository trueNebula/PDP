#include <iostream>
#include <vector>
#include <thread>
#include <future>
#include <mutex>
#include <chrono>

std::vector<int> poly1 = {1, 2, 3, 1, 2, 3, 1, 2, 3};  // coefficients of x^2 + 2x + 3
std::vector<int> poly2 = {4, 5, 4, 5, 0, 5};            // coefficients of 4x + 5

// Regular Polynomial Multiplication (O(n^2))
void regular_multiply() {
    auto start_time = std::chrono::high_resolution_clock::now();

    int degree = poly1.size() + poly2.size() - 2;
    std::vector<int> result(degree + 1, 0);

    for (int i = 0; i < poly1.size(); ++i) {
        for (int j = 0; j < poly2.size(); ++j) {
            result[i + j] += poly1[i] * poly2[j];
        }
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration_ms = std::chrono::duration_cast<std::chrono::nanoseconds>(end_time - start_time);

    int deg = result.size() - 1;
    std::cout << "Regular: " << result[0] << "x^" << deg;

    for (int i = 1; i < result.size(); i++) {
        deg--;

        if (deg > 1) std::cout << " + " << result[i] << "x^" << deg;
        else if (deg == 1) std::cout << " + " << result[i] << "x";
        else std::cout << " + " << result[i];
    }
    std::cout << " - " << duration_ms.count() << "ns" << std::endl;
}

// Parallelized Regular Polynomial Multiplication
void parallel_regular_multiply() {
    int num_threads = 4;
    int degree = poly1.size() + poly2.size() - 2;
    std::vector<int> result(degree + 1, 0);
    std::mutex result_mutex;

    auto multiply_range = [&](int start, int end) {
        for (int i = start; i < end; ++i) {
            for (int j = 0; j < poly2.size(); ++j) {
                std::lock_guard<std::mutex> lock(result_mutex);
                result[i + j] += poly1[i] * poly2[j];
            }
        }
    };

    std::vector<std::thread> threads;
    int chunk_size = poly1.size() / num_threads;

    auto start_time = std::chrono::high_resolution_clock::now();

    for (int i = 0; i < num_threads; ++i) {
        int start = i * chunk_size;
        int end = (i == num_threads - 1) ? poly1.size() : (i + 1) * chunk_size;
        threads.emplace_back(multiply_range, start, end);
    }

    for (auto &thread: threads) {
        thread.join();
    }

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration_ms = std::chrono::duration_cast<std::chrono::nanoseconds>(end_time - start_time);

    int deg = result.size() - 1;
    std::cout << "Parallel: " << result[0] << "x^" << deg;

    for (int i = 1; i < result.size(); i++) {
        deg--;

        if (deg > 1) std::cout << " + " << result[i] << "x^" << deg;
        else if (deg == 1) std::cout << " + " << result[i] << "x";
        else std::cout << " + " << result[i];
    }
    std::cout << " - " << duration_ms.count() << "ns" << std::endl;
}

using namespace std;

// Helper function to add two vectors element-wise
vector<int> add(const vector<int>& A, const vector<int>& B) {
    int n = max(A.size(), B.size());
    vector<int> result(n, 0);
    for (int i = 0; i < A.size(); ++i) {
        result[i] += A[i];
    }
    for (int i = 0; i < B.size(); ++i) {
        result[i] += B[i];
    }
    return result;
}

// Helper function to subtract two vectors element-wise
vector<int> subtract(const vector<int>& A, const vector<int>& B) {
    int n = max(A.size(), B.size());
    vector<int> result(n, 0);
    for (int i = 0; i < A.size(); ++i) {
        result[i] += A[i];
    }
    for (int i = 0; i < B.size(); ++i) {
        result[i] -= B[i];
    }
    return result;
}

// Karatsuba Algorithm Recursive
vector<int> karatsuba(const vector<int>& A, const vector<int>& B) {
    int n = A.size();

    // Base case: if the polynomials are small, use the simple algorithm
    if (n <= 4) {
        vector<int> result(2 * n - 1, 0);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                result[i + j] += A[i] * B[j];
            }
        }
        return result;
    }

    // Split the polynomials into two halves
    int mid = n / 2;
    vector<int> AL(A.begin(), A.begin() + mid);
    vector<int> AH(A.begin() + mid, A.end());
    vector<int> BL(B.begin(), B.begin() + mid);
    vector<int> BH(B.begin() + mid, B.end());

    // Recursive steps
    vector<int> P1 = karatsuba(AL, BL);
    vector<int> P2 = karatsuba(AH, BH);
    vector<int> ALAH = add(AL, AH);
    vector<int> BLBH = add(BL, BH);
    vector<int> P3 = karatsuba(ALAH, BLBH);

    // Combine the results
    vector<int> result = subtract(subtract(P3, P1), P2);

    // Extend the result to the correct size
    result.resize(2 * n - 1, 0);

    // Add the results at the appropriate positions
    for (int i = 0; i < P1.size(); ++i) {
        result[i] += P1[i];
        result[i + mid] += P3[i];
        result[i + 2 * mid] += P2[i];
    }

    return result;
}

vector<int> parallel_karatsuba(const vector<int>& A, const vector<int>& B) {
    int n = A.size();
    std::vector<std::thread> threads;

    // Base case: if the polynomials are small, use the simple algorithm
    if (n <= 4) {
        vector<int> result(2 * n - 1, 0);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                result[i + j] += A[i] * B[j];
            }
        }
        return result;
    }

    // Split the polynomials into two halves
    int mid = n / 2;
    vector<int> AL(A.begin(), A.begin() + mid);
    vector<int> AH(A.begin() + mid, A.end());
    vector<int> BL(B.begin(), B.begin() + mid);
    vector<int> BH(B.begin() + mid, B.end());

    // Recursive steps (parallelized)
    future<vector<int>> p1 = async(launch::async, parallel_karatsuba, AL, BL);
    future<vector<int>> p2 = async(launch::async, parallel_karatsuba, AH, BH);

    // Sequential step
    vector<int> P1 = p1.get();
    vector<int> P2 = p2.get();

    vector<int> ALAH = add(AL, AH);
    vector<int> BLBH = add(BL, BH);
    vector<int> P3 = karatsuba(ALAH, BLBH);

    // Combine the results
    vector<int> result = subtract(subtract(P3, P1), P2);

    // Extend the result to the correct size
    result.resize(2 * n - 1, 0);

    // Add the results at the appropriate positions
    for (int i = 0; i < P1.size(); ++i) {
        result[i] += P1[i];
        result[i + mid] += P3[i];
        result[i + 2 * mid] += P2[i];
    }

    return result;
}

// Karatsuba Algorithm Parent Function
void karatsuba_multiply() {
    while (poly1.size() > poly2.size()) {
        poly2.insert(poly2.begin(), 0);
    }

    while (poly2.size() > poly1.size()) {
        poly1.insert(poly1.begin(), 0);
    }

    auto start_time = std::chrono::high_resolution_clock::now();
    std::vector<int> result = karatsuba(poly1, poly2);

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration_ms = std::chrono::duration_cast<std::chrono::nanoseconds>(end_time - start_time);

    int deg = result.size() - 1;
    std::cout << "Karatsuba: " << result[0] << "x^" << deg;

    for (int i = 1; i < result.size(); i++) {
        deg--;

        if (deg > 1) std::cout << " + " << result[i] << "x^" << deg;
        else if (deg == 1) std::cout << " + " << result[i] << "x";
        else std::cout << " + " << result[i];
    }
    std::cout << " - " << duration_ms.count() << "ns" << std::endl;

}

void parallel_karatsuba_multiply() {
    while (poly1.size() > poly2.size()) {
        poly2.insert(poly2.begin(), 0);
    }

    while (poly2.size() > poly1.size()) {
        poly1.insert(poly1.begin(), 0);
    }

    auto start_time = std::chrono::high_resolution_clock::now();
    std::vector<int> result = parallel_karatsuba(poly1, poly2);

    auto end_time = std::chrono::high_resolution_clock::now();
    auto duration_ms = std::chrono::duration_cast<std::chrono::nanoseconds>(end_time - start_time);

    int deg = result.size() - 1;
    std::cout << "Parallel Karatsuba: " << result[0] << "x^" << deg;

    for (int i = 1; i < result.size(); i++) {
        deg--;

        if (deg > 1) std::cout << " + " << result[i] << "x^" << deg;
        else if (deg == 1) std::cout << " + " << result[i] << "x";
        else std::cout << " + " << result[i];
    }
    std::cout << " - " << duration_ms.count() << "ns" << std::endl;

}

int main() {
    regular_multiply();
    parallel_regular_multiply();
    karatsuba_multiply();
    parallel_karatsuba_multiply();

    return 0;
}
