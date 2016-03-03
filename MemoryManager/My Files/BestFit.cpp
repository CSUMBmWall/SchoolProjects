/*
 *
 *   File Name: BestFit.cpp
 *        Name: Matt Wall
 *      Course: CST 238
 *        Term: Fall 2014
 *  Assignment: Project 2
 *    Abstract: BestFit Base Class
 *
 */

#include <algorithm>

#include "BestFit.h"

// Allocate size number of bytes
// Returns a pointer to the newly
// allocated chunk of memory
//
// Invaraint: newly allocated memory
//            must be zero'ed out
//
//    Errors: requested size is larger
//            than the greatest chunk
void *BestFit::allocate(size_t size) {

	if (freeList.empty()) { throw allocationException; }	

  	size_t bs = BLOCK_SIZE;
	// size must be a multiple of the block size
	//if size < block size, make it block size
	if (size < bs) { size = bs; }

	//if size > block size and it does not fit evenly into a block
	//make the size the next multiple of block size that is larger than size
	else if (size % bs != 0) { size = (size/bs) * bs + bs; }

	//declare variables and set up vectors with chunk sizes and addresses
	list<FreeBlock>::const_iterator n = freeList.begin(), it = freeList.begin();

	//initialize vector vcs with start and end addresses of free chunks
	//initialize vector vs2 with chunk sizes
	vector<pair<const byte *, const byte *> > vcs;
	vector<size_t> vcs2;
	chunkSizes(vcs);
	chunkSizes(vcs2);
	sort (vcs.begin(), vcs.end());

	//find smallest chunk that is as big as the memory needed by caller
	//min is set to MEMORY_SIZE + 1 because a chunk could never be that large
	//if that value is still there after the vector has been traversed
	//there is not a chunk that is big enough
	size_t min = MEMORY_SIZE + 1;
	for (unsigned int i = 0; i < vcs2.size(); i++)
	{
		if (vcs2[i] >= size && vcs2[i] < min) {min = vcs2[i]; }
	}
	//no chunk was found that was big enough
	if (min == MEMORY_SIZE + 1) { throw allocationException; }	

	//need sizes in same order as in memory
	chunkSizes(vcs2);	

	//set void pointer to start address of freeBlock to be allocated
	void* vptr;

	//reset n to beginning of freeList
	n = freeList.begin();

	for (unsigned int i = 0; i < vcs2.size(); i++)
	{
		//advance iterators after first round
		if (i != 0) 
		{ 
			advance(n, vcs2[i-1]/BLOCK_SIZE);
			advance(it, vcs2[i-1]/BLOCK_SIZE);
		}
		
		//if the minimum sized chunk is found, store address in allocmap
		//and erase the appropriate # of blocks from the freeList
		if (min == vcs2[i])
		{
			allocMap[n->start] = size;
			advance (it, size/BLOCK_SIZE);				
			vptr = n->start;
			freeList.erase(n, it);
			return vptr;
		}					
	}
	
  return NULL;
}