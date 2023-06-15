/********************************/
/* Anthony Galczak              */
/* 11-5-16                      */
/* CS-241 Section 001           */
/* Lab 7                        */
/* Working with Binary Search   */
/* Trees and Linked Lists       */
/********************************/

#ifndef BINARYTREE_H
#define BINARYTREE_H

#include <stdio.h>
#include <stdlib.h>
#include "binarytree.h"

struct TreeNode
{
  int data;
  struct TreeNode* left;
  struct TreeNode* right;
};

void printTreeHelper(struct TreeNode* root);
void printLeavesHelper(struct TreeNode* root);
struct TreeNode* rightMostChild(struct TreeNode* root);
int minValueBST(struct TreeNode* root);

/* createNode: */
/* Creates a new TreeNode struct */
/* Returns the new TreeNode struct */
struct TreeNode* createNode(int data)
{
  struct TreeNode* tn = malloc(sizeof(struct TreeNode));

  tn->data = data;
  tn->left = NULL;
  tn->right = NULL;
  return tn;
}

/* insertBST: */
/* Inserting a BST into an existing tree */
/* Returns a BST that represents the tree */
struct TreeNode* insertBST(struct TreeNode* root, int data)
{
  /* If there are no nodes in the root, then return a new single node */
  if(root == NULL)
  {
    return createNode(data);
  }

  /* Insert the node in the proper place */
  if(data < root->data)
  {
    root->left = insertBST(root->left, data);
  }
  else if(data > root->data)
  {
    root->right = insertBST(root->right, data);
  }

  /* First root of the tree was unchanged, return it */
  return root;
}

/* removeBST: */
/* Removes a node from the BST. */
/* Returns 0 if not found, returns 1 if found and removed */
int removeBST(struct TreeNode** rootRef, int data)
{
  struct TreeNode* tn;
  int winCondition = 0;

  if (*rootRef == NULL)
  {
    return 0;
  }

  if(data < (*rootRef)->data)
  {
    winCondition |= removeBST(&((*rootRef)->left), data);
  }
  else if(data > (*rootRef)->data)
  {
    winCondition |= removeBST(&((*rootRef)->right), data);
  }

  /* We found the value */
  else
  {
    if((*rootRef)->left == NULL && (*rootRef)->right == NULL)
    {
      free(*rootRef);
      *rootRef = NULL;
    }
    else if((*rootRef)->left == NULL)
    {
      tn = (*rootRef)->right;
      free(*rootRef);
      *rootRef = tn;
    }
    /* Two children - data == *rootRef->data */
    else
    {
      struct TreeNode* right;
      struct TreeNode* left;

      /* Save children */
      right = (*rootRef)->right;
      left = (*rootRef)->left;
      
      free(*rootRef);
      *rootRef = rightMostChild(left);
      
      if(*rootRef != left)
      {
        (*rootRef)->left = left;
      }

      (*rootRef)->right = right;  
    }

    winCondition = 1;
  }

  return winCondition |= 0;
}

/* rightMostChild: */
/* Getting the right most node so that we can attach it to a */ 
/* Returns the right-most node of any tree */
struct TreeNode* rightMostChild(struct TreeNode* root)
{
  if(root == NULL) return NULL;
  else if(root->right == NULL)
  {
    return root;
  }
  /* Checking two right nodes in advance to prevent circular cycles */
  else if(root->right->right == NULL)
  {
    struct TreeNode* tn = root->right;
    root->right = tn->left;
    return tn;
  }
  else
  {
    return rightMostChild(root->right);
  }
}

/* maxValueBST: */
/* Finds the maximum value throughout the whole BST */
/* Returns an int representing the maximum value */
int maxValueBST(struct TreeNode* root)
{  
  int maxValue, leftVal, rightVal;
  maxValue = leftVal = rightVal = 0;
  
  if(root == NULL)
  {
    return 0;
  }

  maxValue = root->data;

  leftVal = maxValueBST(root->left);
  rightVal = maxValueBST(root->right);
  
  if(leftVal > maxValue)
  {
    maxValue = leftVal;
  }
  if(rightVal > maxValue)
  {
    maxValue = rightVal;
  }

  return maxValue;
}

/* minValueBST: */
/* Finds the minimum value throughout the whole BST */
/* Returns an int representing the minimum value */
int minValueBST(struct TreeNode* root)
{  
  int minValue, leftVal, rightVal;
  leftVal = rightVal = minValue = 0;
  
  if(root == NULL)
  {
    /* Return max int value if root is null */
    return 0x7FFFFFFF;
  }

  minValue = root->data;

  leftVal = minValueBST(root->left);
  rightVal = minValueBST(root->right);
  
  if(leftVal < minValue)
  {
    minValue = leftVal;
  }
  if(rightVal < minValue)
  {
    minValue = rightVal;
  }

  return minValue;
}

/* maxDepth: */
/* Returns the maximum height or depth of the BST as an int */
int maxDepth(struct TreeNode* root)
{
  int rightVal, leftVal;
  rightVal = leftVal = 0;
  /* Tree is empty, no depth */
  if(root == NULL)
  {
    return 0;
  }
  else
  {
    rightVal = maxDepth(root->right);
    leftVal = maxDepth(root->left);

    if (leftVal > rightVal) return leftVal + 1;
    else return rightVal + 1;
  }
}

/* isBalanced: */
/* Figures out if the tree is balanced or not. The tree is balanced */
/* if the diff in sub-trees is no more than 1 */
/* Return 1 if tree is balanced, 0 if it isn't balanced. */
int isBalanced(struct TreeNode* root)
{
  int leftVal, rightVal;
  leftVal = rightVal = 0;

  if(root == NULL)
  {
    return 1;
  }

  leftVal = maxDepth(root->left);
  rightVal = maxDepth(root->right);

  if(abs(leftVal-rightVal) < 2 && isBalanced(root->left) && isBalanced(root->right))
  {
    return 1;
  }
  else
  {
    return 0;
  }

}

/* isBST: */
/* Checks if the BST is a valid tree */
/* Returns 1 if the tree is valid, 0 if not valid */
int isBST(struct TreeNode* root)
{
  if(root == NULL) return 1;

  if(root->left != NULL && maxValueBST(root->left) > root->data)
  {
    return 0;
  }
  
  if(root->right != NULL && minValueBST(root->right) < root->data)
  {
    return 0;
  }

  if(!isBST(root->right) || !isBST(root->left))
  {
    return 0;
  }

  return 1;

}

/* printTreeHelper: */
/* Prints the entire BST in-order traversal using recursion */
/* Returns nothing */
void printTreeHelper(struct TreeNode* root)
{
  if(root != NULL)
  {
    printTreeHelper(root->left);
    printf("%d ", root->data);
    printTreeHelper(root->right);
  }
}

/* printTree: */
/* Calls printTreeHelper and then prints a new line */
/* Returns nothing */
void printTree(struct TreeNode* root)
{
  printTreeHelper(root);
  printf("\n");
}

/* printLeavesHelper: */
/* Prints the leaves of the BST using recursion */
/* Returns nothing */
void printLeavesHelper(struct TreeNode* root)
{
  if(root->left == NULL && root->right == NULL)
  {
    printf("%d ", root->data);
  }
  
  if(root->left != NULL)
  {
    printLeavesHelper(root->left);
  }

  if(root->right != NULL)
  {
    printLeavesHelper(root->right);
  }

}

/* printLeaves: */
/* Calls printLeavesHelper and then prints a new line */
/* Returns nothing */
void printLeaves(struct TreeNode* root)
{
  printLeavesHelper(root);
  printf("\n");
}

/* freeTree: */
/* Freeing memory of all the nodes */
/* Returns nothing */
void freeTree(struct TreeNode* root)
{
  if(root == NULL)
  {
    return;
  }
  freeTree(root->left);
  freeTree(root->right);
  free(root);
}
#endif
