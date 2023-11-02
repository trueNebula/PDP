#include <iostream>
#include <thread>
#include <vector>
#include <condition_variable>

std::vector<int> vectorA = {1, 2, 3, 4, 5, 6, 7, 8, 9};
std::vector<int> vectorB = {2, 4, 6, 8, 0, 2, 4, 6, 8};
std::vector<int> result(vectorA.size(), 0);

std::mutex mutex;
std::condition_variable cv;

int producerIndex = 0;
int lastConsumedIndex = 0;

void produce() {
    std::cout << "Producer Started!" << std::endl;

    while (true) {
        std::unique_lock<std::mutex> lock(mutex);
        if (producerIndex >= vectorA.size()) {
            cv.notify_all();
            break;
        }

        result[producerIndex] = vectorA[producerIndex] * vectorB[producerIndex];
        std::cout << "Produced value: " << result[producerIndex] << " from index " << producerIndex << std::endl;
        producerIndex++;
        std::cout << "prod index " << producerIndex << std::endl;
        cv.notify_one();
    }
}

void consume() {
    std::cout << "Consumer Started!" << std::endl;

    int sum = 0;

    while (true) {
        std::cout << "cons index " << lastConsumedIndex << std::endl;

        std::unique_lock<std::mutex> lock(mutex);

        cv.wait(lock, [] { return producerIndex >  lastConsumedIndex ||  lastConsumedIndex >= vectorA.size(); });
        sum += result[lastConsumedIndex];

        std::cout << "Consumed value: " << result[lastConsumedIndex] << " from index " << lastConsumedIndex << std::endl;
        lastConsumedIndex++;

        if (lastConsumedIndex >= vectorA.size()) {
            break;
        }
    }

    std::cout << "Consumer finished with value: " << sum << std::endl;
}

int main() {
    std::thread producer(produce);
    std::thread consumer(consume);

    producer.join();
    consumer.join();

    return 0;
}
