package com.example.multiball;

public class ContainerBox {
	   int minX, maxX, minY, maxY;  // Box's bounds (package access)
	   
	   /** Constructors */
	   public ContainerBox(int x, int y, int width, int height) {
	      minX = x;
	      minY = y;
	      maxX = x + width -1;
	      maxY = y + height -1;
	   }
	   
	   /** Set or reset the boundaries of the box. */
	   public void set(int x, int y, int width, int height) {
	      minX = x;
	      minY = y;
	      maxX = x + width ;
	      maxY = y + height ;
	   }

	   
	}
