/*
 *
 *   File Name: MemoryManager.cpp
 *        Name: 
 *      Course: CST 238
 *        Term: Fall 2014
 *  Assignment: Project 2
 *    Abstract: MemoryManager Super Class
 *
 */

#include <cmath>      // pow
#include <cstring>    // memset
#include <iostream>
#include <vector>

#include "MemoryManager.h"

using std::cout;
using std::ostream;
using std::vector;

// Utility function used by the constructor
unsigned int bytes(size_t num, memory_unit_t unit) {
  return (unsigned int) pow(2.0, (double) unit);
}

/*
 *
 *   MemoryManager Method Definitions
 *
 */
// Constructor: initializes the freeList
MemoryManager::MemoryManager(size_t total_bytes, size_t block_size) {
  // Check for the default argument
  if (!total_bytes) {
    block_size = 64;
    MEMORY_SIZE = bytes(16, MB);
  } else {
    // Must be a multiple of the block size
    if (total_bytes % block_size != 0) { 
      total_bytes += (total_bytes % block_size);
    }
    MEMORY_SIZE = total_bytes;
  }

  // Allocate memory and zero it out
  memory = new byte[MEMORY_SIZE];
  memset(memory, 0, MEMORY_SIZE);

  // Store the block size
  BLOCK_SIZE = block_size;

  // Add all FreeBlocks to the freeList
  for (unsigned i = 0; i < MEMORY_SIZE; i += BLOCK_SIZE) {
    freeList.push_back(FreeBlock(memory + i, 
                                 memory + i + BLOCK_SIZE - 1));
  }
}

// A chunk is a contiguous sequence of free blocks
// Stores a list of contiguous chunk sizes in
// the vector passed in
// You can assume the vector passed in is empty
void MemoryManager::chunkSizes(vector<size_t> &vcs) const {
  // Your code here
}

// Stores starting and ending addresses
// of all free chunks in memory as pairs
void MemoryManager::chunkSizes(vector<pair<const byte *, 
                                const byte *> > &vcs) const {
  // Your code here
}

// Returns the address of the first free block
// Or NULL if all of memory has been allocated
byte *MemoryManager::firstFreeBlock() const {
  // Your code here
  return NULL;
}

// Free previously allocated memory
//
// Invariant: freed memory must be zero'd out
//
//    Errors: trying to free memory not currently
//            allocated
//            trying to free memory not in the range
//            [memory start, memory start + MEMORY_SIZE)
void MemoryManager::free(void *ptr) {
  // Your code here
}
    
// Returns the size in bytes of the largest chunk available
// A chunk is defined as >= 2 contiguous blocks
// Return 0 if only single blocks are available
size_t MemoryManager::largestChunkAvailable() const {
  // Your code here
  return 0;
}

// Returns the number of bytes available for allocation
size_t MemoryManager::memoryAvailable() const {
  // Your code here
  return 0;
}

// Returns the number of allocated blocks
unsigned MemoryManager::numAllocatedBlocks() const {
  // Your code here
  return 0;
}

// Returns the number of free blocks
unsigned MemoryManager::numFreeBlocks() const {
  // Your code here
  return 0;
}

// Returns in the size in bytes of the smallest chunk available
// A chunk is defined as >= 2 contiguous blocks
// Return 0 if only individual blocks are available
size_t MemoryManager::smallestChunkAvailable() const {
  // Your code here
  return 0;
}

ostream& operator<<(ostream& os, const MemoryManager& mm) {
  os << "[Free Blocks]: " 
      << mm.numFreeBlocks()
      << " | [Allocated Blocks]: "
      << mm.numAllocatedBlocks()
      << " | [Available Memory]: "
      << mm.memoryAvailable()
      << "\n";
  return os;
}
