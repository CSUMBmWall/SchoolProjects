/*
 *
 *   File Name: MemoryManager.cpp
 *        Name: Matt Wall
 *      Course: CST 238
 *        Term: Fall 2014
 *  Assignment: Project 2
 *    Abstract: MemoryManager Super Class
 *
 */

#include <algorithm>
#include <cmath>      // pow
#include <cstring>    // memset
#include <exception>
#include <iostream>
#include <iterator>
#include <utility>
#include <vector>

#include "MemoryManager.h"

using std::exception;
using std::cout;
using std::ostream;
using std::vector;
using std::next;
using std::make_pair;
using std::pair;

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

	//clear vector contents
	vcs.clear();

	// Return if there are 1 or 0 freeBlocks
	if (freeList.empty() || numFreeBlocks() == 1) { return;	}

	//set value of s to address of start  and e to address of end on first FreeBlock in freeList
	list<FreeBlock>::const_iterator n = freeList.begin();	
	
	byte *s = n->start;
	byte *e = n->end;
	size_t chunk = BLOCK_SIZE;

	//Use to keep track of block size being pushed to vector
	//in case chunk includes last item in freeList
	bool pushed = false;

	//start loop 
	for (list<FreeBlock>::const_iterator it = next(n); it != freeList.end(); ++it) 
	{
		pushed = false;
		//if starting address of new freeList node == the end address + 1 of the last node, set e to the new end and add another block to the chunk size
		//else store chunk size in vector and reset s
		if (it->start == e + 1)
		{
			e = it->end;
			chunk += BLOCK_SIZE;
		//chunk has ended, add to vector and reset pointers to start and end of current block in freeList
		} else if (chunk > BLOCK_SIZE) {			
			vcs.push_back(chunk);
			pushed = true;
			chunk = BLOCK_SIZE;
			s = it->start;
			e = it->end;
		//there was only one free block so reset pointers to start and end of current block in freeList
		} else {
			s = it->start;
			e = it->end;
		}	
	}
	//adds chunk to vector if last block is free -
	if (pushed == false) { vcs.push_back(chunk); }
}

// Stores starting and ending addresses
// of all free chunks in memory as pairs
void MemoryManager::chunkSizes(vector<pair<const byte *, const byte *> > &vcs) const {

	//clear vector contents
	vcs.clear();

	// Return if there are 1 or 0 freeBlocks
	if (freeList.empty() || numFreeBlocks() == 1) { return;	}

	//set value of s to address of start  and e to address of end on first FreeBlock in freeList
	list<FreeBlock>::const_iterator n = freeList.begin();	
	
	//initialize pointers to beginning addresses in current FreeBlock, set chunk to block size and create pair to store 
	//start and end addresses of chunk
	byte *s = n->start;
	byte *e = n->end;
	size_t chunk = BLOCK_SIZE;
	pair <byte*, byte*> chunky;

	//Use to keep track of block size being pushed to vector
	//in case chunk goes to end of the list
	bool pushed = false;

	//start loop 
	for (list<FreeBlock>::const_iterator it = next(n); it != freeList.end(); ++it) 
	{
		pushed = false;
		//if starting address of new freeList node == the end address + 1 of the last node, 
		//set e to the new end and add another block to the chunk size else store chunk size in vector and reset s
		if (it->start == e + 1)
		{
			e = it->end;
			chunk += BLOCK_SIZE;
		//if there are more than 1 FreeBlocks, make pair and add to vcs, 
		//change pushed to true, reset chunk to block size and s and e pointers to start and end of current FreeBlock
		} else if (chunk > BLOCK_SIZE) {	
			chunky = make_pair(s,e);
			vcs.push_back(chunky);
			pushed = true;
			chunk = BLOCK_SIZE;
			s = it->start;
			e = it->end;
		//only single FreeBlock
		} else {
			s = it->start;
			e = it->end;
		}	
	}
	//makes pair of start and end address to push to vcs vector when the last FreeBlock 
	//is included in the chunk
	if (pushed == false) 
	{ 
		chunky = make_pair(s,e);
		vcs.push_back(chunky); 
	}
}

// Returns the address of the first free block
// Or NULL if all of memory has been allocated
byte *MemoryManager::firstFreeBlock() const {
	if (freeList.empty()) { return NULL; }
	list<FreeBlock>::const_iterator it = freeList.begin();	
	return it->start;	
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

	//create vectors to hold chunk sizes and start and end addresses of chunks
	vector<pair<const byte *, const byte *> > vcs;
	vector<size_t> vcs2;	

	//crate byte pointer and set it to the memory address pointer passed in
	//this ptr will be used to find values in allocMap
	byte* bptr;
	bptr = static_cast<byte*>(ptr);

	//if address is not in map
	if (!allocMap.count(bptr)) { throw freeException; }

	//address was found, remove it from allocMap
	else
	{
		size_t size = allocMap.find(bptr)->second;
		allocMap.erase(bptr);

		// Add freed blocks to the freeList
		for (unsigned i = 0; i < size; i += BLOCK_SIZE) 
		{
			freeList.push_back(FreeBlock(bptr + i, bptr + i + BLOCK_SIZE - 1));
		}
		sortFreeList();
	}
}
    
// Returns the size in bytes of the largest chunk available
// A chunk is defined as >= 2 contiguous blocks
// Return 0 if only single blocks are available
//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!I also return 0 if the freeList is empty!!!!!!!!!!!!!!!!!!!!!!
size_t MemoryManager::largestChunkAvailable() const {

	if (freeList.empty() || numFreeBlocks() == 1) { return 0; }
	//set value of s to address of start  and e to address of end on first FreeBlock in freeList
	list<FreeBlock>::const_iterator n = freeList.begin();	
	
	//initialize byte pointers for start and end addresses of blocks 
	//and size_t variables for chunk and maxChunk
	byte *s = n->start;
	byte *e = n->end;
	size_t chunk = BLOCK_SIZE;
	size_t maxChunk = chunk;

	//start loop 
	for (list<FreeBlock>::const_iterator it = next(n); it != freeList.end(); ++it) 
	{
		//if starting address of new freeList item == the end of the last, set e to the new end and add another block to the chunk size
		//else store chunk size in vector and reset s
		if (it->start == e + 1)
		{
			e = it->end;
			chunk += BLOCK_SIZE;
			if (chunk > maxChunk) { maxChunk = chunk; }
		//chunk has ended, reset chunk and start and end pointers
		} else {			
			chunk = BLOCK_SIZE;
			s = it->start;
			e = it->end;
		}
	}
  return maxChunk;
}

// Returns the number of bytes available for allocation
size_t MemoryManager::memoryAvailable() const {
	return MEMORY_SIZE - numAllocatedBlocks() * BLOCK_SIZE;
}

size_t MemoryManager::memSize() const {
	return MEMORY_SIZE;
}

// Returns the number of allocated blocks
unsigned MemoryManager::numAllocatedBlocks() const {
	if (freeList.empty()) { return MEMORY_SIZE/BLOCK_SIZE; }
	
	else { return MEMORY_SIZE/BLOCK_SIZE - numFreeBlocks(); }
}

// Returns the number of free blocks
unsigned MemoryManager::numFreeBlocks() const {

	if (freeList.empty()) { return 0; }
	unsigned sum = 0;
	for (list<FreeBlock>::const_iterator it = freeList.begin(); it != freeList.end(); ++it) 
	{
		sum += 1;
	}
	return sum;
}

// Returns in the size in bytes of the smallest chunk available
// A chunk is defined as >= 2 contiguous blocks
// Return 0 if only individual blocks are available
// !!!!!!!!!!!!!!!!!!!!!!!!!I also return 0 if the list is empty!!!!!!!!!!!!!!!!!!!!!!!
size_t MemoryManager::smallestChunkAvailable() const {
		//set value of s to address of start  and e to address of end on first FreeBlock in freeList
	if (freeList.empty() || numFreeBlocks() == 1){ return 0; } 	
		
	//set value of s to address of start  and e to address of end on first FreeBlock in freeList
	list<FreeBlock>::const_iterator n = freeList.begin();	
	
	//initialize byte pointers for start and end addresses of FreeBlock
	//as well as chunk for chunk size and min for minimum chunk
	byte *s = n->start;
	byte *e = n->end;
	size_t chunk = BLOCK_SIZE;
	size_t min = BLOCK_SIZE * 2;

	//start loop 
	for (list<FreeBlock>::const_iterator it = next(n); it != freeList.end(); ++it) 
	{
		//if starting address of new freeList item == the end of the last, set e to the new end and add another block to the chunk size
		//else store chunk size in vector and reset s
		if (it->start == e + 1)
		{
			e = it->end;
			chunk += BLOCK_SIZE;
			if (chunk >= min) { return min; }
		//chunk has ended - reset chunk to BLOCK_SIZE and start and end addresses to current FreeBlock
		} else {			
			chunk = BLOCK_SIZE;
			s = it->start;
			e = it->end;
		}		
	}
	if (chunk >= min) { return min; }
	else { return 0; }
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