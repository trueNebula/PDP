#include <iostream>
#include <thread>
#include <vector>
#include <mutex>
#include <condition_variable>
#include <random>

// Constants
const int numProducts = 5;
const double productPrices[] = {1.0, 2.0, 3.0, 4.0, 5.0};
const int initialQuantities[] = {100, 200, 150, 300, 50};

// Shared data
std::vector<int> productQuantities(numProducts);
double totalMoney = 0;
std::vector<std::vector<std::pair<int, int>>> salesRecord;

// Mutexes
std::mutex productMutex[numProducts];
std::mutex moneyMutex;
std::mutex salesMutex;

// Condition variable for inventory check
std::condition_variable inventoryCheckCV;

// Function to perform a sale
void performSale(int product, int quantity) {
    double saleAmount = productPrices[product] * quantity;

    std::lock_guard<std::mutex> productLock(productMutex[product]);

    if (productQuantities[product] >= quantity) {
        productQuantities[product] -= quantity;
    } else {
        std::cerr << "Error: Not enough quantity for product " << product << std::endl;
        return;
    }

    std::lock_guard<std::mutex> moneyLock(moneyMutex);
    totalMoney += saleAmount;

    std::cout<<"Performed sale: Product " << product << " | Quantity: " << quantity << " | Remaining: " << \
        productQuantities[product] << "| Money: " <<totalMoney << std::endl;

    std::lock_guard<std::mutex> salesLock(salesMutex);
    salesRecord.push_back({{product, quantity}});
}

// Function to check inventory
void checkInventory() {
    std::cout << "Performing Inventory check" << std::endl;
    double totalSales = 0;
    for (const auto& sale : salesRecord) {
        for (const auto& item : sale) {
            totalSales += productPrices[item.first] * item.second;
            std::cout<<"Current total: " << totalSales << std::endl;
        }
    }

    if (totalSales == totalMoney) {
        std::cout << "Inventory check passed." << std::endl;
    } else {
        std::cerr << "Inventory check failed." << std::endl;
    }
}

// Function for sale thread
void saleThread() {
    std::random_device rd;
    std::mt19937 gen(rd());
    std::uniform_int_distribution<int> productDist(0, numProducts - 1);
    std::uniform_int_distribution<int> quantityDist(1, 10);

    for (int i = 0; i < 10; i++) {
        int product = productDist(gen);
        int quantity = quantityDist(gen);
        performSale(product, quantity);
    }

    // Notify inventory check
    inventoryCheckCV.notify_one();
}

int main() {
    for (int i = 0; i < numProducts; i++) {
        productQuantities[i] = initialQuantities[i];
    }

    int numThreads = 4;
    std::vector<std::thread> threads;
    for (int i = 0; i < numThreads; i++) {
        threads.push_back(std::thread(saleThread));
    }

    // Wait for all sale threads to finish
    for (auto& thread : threads) {
        thread.join();
    }

    // Perform inventory check
    std::unique_lock<std::mutex> inventoryCheckLock(salesMutex);
    inventoryCheckCV.wait(inventoryCheckLock, [] { return !salesRecord.empty(); });
    checkInventory();

    return 0;
}
