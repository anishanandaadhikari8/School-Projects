/********************************/
/* Anthony Galczak              */
/* 11-5-16                      */
/* CS-241 Section 001           */
/* Lab 7                        */
/* Working with Binary Search   */
/* Trees and Linked Lists       */
/********************************/

#ifndef LINKEDLIST_H
#define LINKEDLIST_H

#include <stdio.h>
#include <stdlib.h>
#include "linkedlist.h"

struct ListNode* reverseHelper(struct ListNode** headRef);

struct ListNode
{
  int data;
  struct ListNode* next;
};

/* createNode: */
/* Creates a ListNode node with malloc */
/* Returns: ListNode with data parameter and NULL addressor */
struct ListNode* createNode(int data)
{
  struct ListNode* ln = malloc(sizeof(struct ListNode));
  
  ln->data = data;
  ln->next = NULL;
  
  return ln;
}

/* insertSorted: */
/* Inserts a given node into a sorted list in order */
/* This item gets inserted via a recursive call */
/* Returns: New head of the list */
struct ListNode* insertSorted(struct ListNode* head, int data)
{
  /* When head is NULL, short circuits to return ln with NULL next */
  /* Otherwise if data is less than next nodes data return new node */
  if(head == NULL || data < head->data)
  {
    struct ListNode* ln = createNode(data);
    ln->next = head;
    return ln;
  }
  else
  {
    head->next = insertSorted(head->next, data);
    return head;
  }
  
}

/* removeItem */
/* Searches the whole linked list and removes a given item via data */
/* removeItem works via a revursive call if the given *headRef isn't NULL */
/* Returns: 1 for success, 0 for failure */
int removeItem(struct ListNode** headRef, int data)
{
  if((*headRef) == NULL)
  {
    return 0;
  }
  else if((*headRef)->data == data)
  {
    struct ListNode* tmp = *headRef;
    *headRef = (*headRef)->next;
    free(tmp);
    return 1;
  }
  else
  {
    return removeItem(&((*headRef)->next), data);
  }

}

/* pushStack: */
/* Pushes a new node onto the head of the list */
/* Returns: The new head of the list */
struct ListNode* pushStack(struct ListNode* head, int data)
{
  struct ListNode* ln = createNode(data); 
  ln->next = head;
  return ln;
}

/* popStack: */
/* Gets the data of the head and then changes the head to the next item in the list */
/* Returns: The data value of the original head */
int popStack(struct ListNode** headRef)
{
  struct ListNode* node = *headRef;
  int data = node->data;
  *headRef = node->next;
  free(node);
  return data;
}

/* listLength: */
/* Gets the total length of the linked list, this works via a recursive call */
/* Returns: The list length */
int listLength(struct ListNode* head)
{ 
  if(head == NULL)
  {
    return 0;
  } 
  else
  {
     return listLength(head->next) + 1;
  }
}

/* printList: */
/* Prints the whole linked list in "val val val val \n" format */
/* Returns nothing */
void printList(struct ListNode* head)
{
  if(head == NULL)
  {
    printf("\n");
  }
  else
  {
    printf("%d ", head->data);
    printList(head->next);
  }
}

/* freeList: */
/* Iteratively frees all the nodes in the list */
/* Returns nothing */
void freeList(struct ListNode* head)
{
  struct ListNode* ln;
  while(head != NULL)
  {
    ln = head;
    head = head->next;
    free(ln);
  }
}


/* reverseHelper: */
/* Reverses each node in place via recursion */
/* Returns the tail of the list */
struct ListNode* reverseHelper(struct ListNode** headRef)
{
  struct ListNode* tail;

  if((*headRef)->next != NULL)
  {
    struct ListNode* previous = *headRef;
    *headRef = ((*headRef)->next);
    tail = reverseHelper(headRef);

    tail->next = previous;
    return previous;
    
  }
  else
  {
    return *headRef;
  }
}

/* reverseList: */
/* Reverses the whole linked list from head to tail */
/* Returns nothing */
void reverseList(struct ListNode** headRef)
{

  /* Catching if the list is empty or somehow we get to a null reference */
  if(headRef == NULL || *headRef == NULL)
  {
    return;
  }
  else
  {
    /* Calling the reverseHelper to reverse the list and then assigning it's next to NULL */
    reverseHelper(headRef)->next = NULL ;
  }
}


#endif

