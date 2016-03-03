/*
 *   
 *   File Name: FirstFit.cpp
 *        Name: Matt Wall
 *      Course: CST 238
 *        Term: Fall 2014
 *  Assignment: Project 2
 *    Abstract: FirstFit Base Class
 *
 */

#include <iterator>
#include <map>

#include "FirstFit.h"

using namespace std;

// Allocate size number of bytes
// Returns a pointer to the newly
// allocated chunk of memory
//
// Invaraint: newly allocated memory
//            must be zero'ed out
//
//    Errors: requested size is larger
//            than the greatest chunk
void *FirstFit::allocate(size_t size) 
{
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

	//void pointer to point to beginning of list
	void* vptr;
	vptr = n->start;	

	//if size == block size, use first free block
	if (size == BLOCK_SIZE) 
	{		
		allocMap[n->start] = size;
		freeList.pop_front();	
		return vptr;		
	}

	//if there is not enough memory or no memory, throw exception
	else if (size > largestChunkAvailable() || freeList.empty()) { throw allocationException; }	

	//otherwise iterate through list until block is found that is 
	//large enough and remove blocks from free list and allocMap
	else 
	{
		for (unsigned int i = 0; i < vcs2.size(); i++)
		{
			//advance iterators after first round
			if (i != 0) 
			{ 
				advance(n, vcs2[i-1]/BLOCK_SIZE);
				advance(it, vcs2[i-1]/BLOCK_SIZE);
			}
			//if chunk is bigger than size, assign first block of chunk, store address in allocmap
			//and erase the appropriate # of blocks from the freeList
			if (size <= vcs2[i])
			{
				allocMap[n->start] = size;
				advance (it, size/BLOCK_SIZE);				
				vptr = n->start;
				freeList.erase(n, it);
				return vptr;
			}					
		}
	}
	return NULL;	
}