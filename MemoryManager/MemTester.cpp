/*
 *
 *   File Name: MemTester.cpp
 *        Name: Matt Wall
 *      Course: CST 238
 *        Term: Fall 2014
 *  Assignment: Project 2
 *    Abstract: Used for testing MemoryManager super class 
 *              as well as FirstFit and BestFit allocation methods
 *
 */

#include <iostream>

#include "BestFit.h"
#include "FirstFit.h"

using namespace std;

int main()
{
	BestFit bf(10,1);

	void *b1, *b2, *b3, *b4, *b5, *b6;

	b1 = bf.allocate(10);
	cout << bf;

	bf.free(b1);
	cout << bf;	

	/*
	
	b2= bf.allocate(10);
	cout << bf;
	
	b3 = bf.allocate(20);
	cout << bf;

	b3 = bf.allocate(10);
	cout << bf;

	b4 = bf.allocate(20);
	cout << bf;

	b5 = bf.allocate(30);
	cout << bf;

	

	
	bf.free(b1);
	cout << bf;	

	bf.free(b4);
	cout << bf;

	bf.free(b5);
	cout << bf;	
	
	FirstFit ff(100,10);
	//cout << ff << endl;

	void * f1, *f2, *f3, *f4, *f5, *f6;
	
	f1= ff.allocate(10);
	cout << ff;
	
	f2 = ff.allocate(20);
	cout << ff;

	f3 = ff.allocate(10);
	cout << ff;

	f4 = ff.allocate(20);
	cout << ff;

	f5 = ff.allocate(40);
	cout << ff;

	ff.free(f1);
	cout << ff;	

	ff.free(f3);
	cout << ff;	

	ff.free(f2);
	cout << ff;

	ff.free(f4);
	cout << ff;	

	ff.allocate(1);
	cout << ff;
	ff.allocate(3);
	cout << ff;
	
	
	ff.allocate(1);
	cout << ff;
	*/

}