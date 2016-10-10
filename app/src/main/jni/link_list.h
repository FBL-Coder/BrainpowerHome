#ifndef LINK_LIST_H
#define LINK_LIST_H

#include "udppropkt.h"

#include <sys/socket.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <arpa/inet.h>

/**
 * The Node struct,
 * contains item and the pointers that point to previous node/next node.
 */

typedef struct nodercu {
    RCU_INFO item;
    // previous node
    struct nodercu* prev;
    // next node
    struct nodercu* next;
} node_rcu;
/**
 * The rcu_linked_list struct, contains the pointers that
 * point to first node and last node, the size of the rcu_linked_list,
 * and the function pointers.
 */
typedef struct rcu_linked_list {
    node_rcu* head;
    node_rcu* tail;
    // size of this rcu_linked_list
    int size;

    // rcu_add item to any position
    void (*rcu_add) (struct rcu_linked_list*, RCU_INFO rcu_info, int);
    // rcu_add item after tail
    void (*rcu_addLast) (struct rcu_linked_list*, RCU_INFO rcu_info);
    // rcu_add item before head
    void (*rcu_addFirst) (struct rcu_linked_list*, RCU_INFO rcu_info);

    // insert node
    void (*insertBefore) (struct rcu_linked_list*, node_rcu*, node_rcu*);
    // get item from any position
    RCU_INFO (*rcu_get) (struct rcu_linked_list*, u8 *devUnitID, u8* devUnitPass);
    // remove item from any position
    void (*rcu_remove) (struct rcu_linked_list*, u8 *devUnitID);

    // display all element in the rcu_linked_list
    void (*rcu_display) (struct rcu_linked_list*);
    // create a node with item
    node_rcu* (*rcu_create_node) (RCU_INFO rcu_info);
} rcu_linked_list;

/** rcu_add item to any position
 */
void rcu_add (rcu_linked_list* _this, RCU_INFO rcu_info, int position);
/** rcu_add item to head
 */
void rcu_addFirst (rcu_linked_list* _this, RCU_INFO rcu_info);
/** rcu_add item to tail
 */
void rcu_addLast (rcu_linked_list* _this, RCU_INFO rcu_info);
/** get item from specific position
 */
RCU_INFO rcu_get(rcu_linked_list* _this, u8 *devUnitID, u8 *devUnitPass);
/** get item and remove it from any position
 */
void rcu_remove (rcu_linked_list* _this, u8 *devUnitID);
/** display the items in the list
 */
void rcu_display (rcu_linked_list* _this);
/** create a rcu_linked_list
 */
rcu_linked_list rcu_create_linked_list ();
/** create a node_rcu
 */
node_rcu* rcu_create_node (RCU_INFO rcu_info);


typedef struct Node {
    u8 devUnitID[12];
    WARE_DEV ware_dev;
    // previous node
    struct Node* prev;
    // next node
    struct Node* next;
} Node;
/**
 * The LinkedList struct, contains the pointers that
 * point to first node and last node, the size of the LinkedList,
 * and the function pointers.
 */
typedef struct ware_linked_list {
    Node* head;
    Node* tail;
    // size of this LinkedList
    int size;

    // ware_add item to any position
    void (*ware_add) (struct ware_linked_list*, WARE_DEV, u8 *, int);
    // ware_add item after tail
    void (*ware_addLast) (struct ware_linked_list*, WARE_DEV, u8 * );
    // ware_add item before head
    void (*ware_addFirst) (struct ware_linked_list*, WARE_DEV, u8 * );

    // get item from any position
    WARE_DEV (*ware_get) (struct ware_linked_list*, WARE_DEV, u8 * );

    // remove item from any position
    void (*ware_remove) (struct ware_linked_list*, WARE_DEV, u8 * );

    // display all element in the LinkedList
    void (*ware_display) (struct ware_linked_list*);
    // create a node with item
    Node* (*ware_create_node) (WARE_DEV ware_dev, u8 *);
} ware_linked_list;

/** ware_add item to any position
 */
void ware_add (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID, int position);
/** ware_add item to head
 */
void ware_addFirst (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** ware_add item to tail
 */
void ware_addLast (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** get item from specific position
 */
WARE_DEV ware_get(ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** get item and remove it from any position
 */
void ware_remove (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);

/** display the items in the list
 */
void ware_display (ware_linked_list* _this);
/** create a LinkedList
 */
ware_linked_list ware_create_linked_list ();
/** create a Node
 */
Node* ware_create_node (WARE_DEV ware_dev, u8 *devUnitID);


typedef struct NodeAircond {
    u8 devUnitID[12];
    WARE_DEV ware_dev;
    DEV_PRO_AIRCOND aircond;
    // previous node
    struct NodeAircond* prev;
    // next node
    struct NodeAircond* next;
} Node_aircond;


typedef struct aircond_linked_list {
    Node_aircond* head;
    Node_aircond* tail;
    // size of this LinkedList
    int size;

    // ware_add item to any position
    void (*ware_aircond_add) (struct aircond_linked_list*, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID, int position);
    // ware_aircond_add item after tail
    void (*ware_aircond_addLast) (struct aircond_linked_list*, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID);
    // ware_aircond_add item before head
    void (*ware_aircond_addFirst) (struct aircond_linked_list*, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID);

    // get item from any position
    Node_aircond *(*ware_aircond_get) (struct aircond_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // remove item from any position
    void (*ware_aircond_remove) (struct aircond_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // display all element in the LinkedList
    void (*ware_aircond_display) (struct aircond_linked_list*);
    // create a node with item
    Node_aircond* (*ware_aircond_createNode) (WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID);

} aircond_linked_list;
\
/** ware_add item to any position
 */
void ware_aircond_add(aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID, int position);
/** ware_add item to head
 */
void ware_aircond_addFirst (aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID);
/** ware_add item to tail
 */
void ware_aircond_addLast (aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID);

/** get item from specific position
 */
Node_aircond *ware_aircond_get(aircond_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** get item and remove it from any position
 */
void ware_aircond_remove (aircond_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** display the items in the list
 */
void ware_aircond_display (aircond_linked_list* _this);
/** create a LinkedList
 */
aircond_linked_list ware_aircond_create_linked_list ();

typedef struct Nodelight {
    u8 devUnitID[12];
    WARE_DEV ware_dev;
    DEV_PRO_LIGHT light;
    // previous node
    struct Nodelight* prev;
    // next node
    struct Nodelight* next;
} Node_light;


typedef struct light_linked_list {
    Node_light* head;
    Node_light* tail;
    // size of this LinkedList
    int size;

    // ware_add item to any position
    void (*ware_light_add) (struct light_linked_list*, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID, int);
    // ware_aircond_add item after tail
    void (*ware_light_addLast) (struct light_linked_list*, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID);
    // ware_aircond_add item before head
    void (*ware_light_addFirst) (struct light_linked_list*, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID);

    // get item from any position
    Node_light * (*ware_light_get) (struct light_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // remove item from any position
    void (*ware_light_remove) (struct light_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // display all element in the LinkedList
    void (*ware_light_display) (struct light_linked_list*);

    // create a node with item
    Node_light* (*ware_light_createNode) (WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID);
} light_linked_list;

/** ware_add item to any position
 */
void ware_light_add(light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID, int position);
/** ware_add item to head
 */
void ware_light_addFirst (light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID);
/** ware_add item to tail
 */
void ware_light_addLast (light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT light, u8 *devUnitID);

/** get item from specific position
 */
Node_light *ware_light_get(light_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** get item and remove it from any position
 */
void ware_light_remove (light_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** display the items in the list
 */
void ware_light_display (light_linked_list* _this);
/** create a LinkedList
 */
light_linked_list ware_light_create_linked_list ();


typedef struct node_curtain {
    u8 devUnitID[12];
    WARE_DEV ware_dev;
    DEV_PRO_CURTAIN curtain;
    // previous node
    struct node_curtain* prev;
    // next node
    struct node_curtain* next;
} node_curtain;


typedef struct curtain_linked_list {
    node_curtain* head;
    node_curtain* tail;
    // size of this LinkedList
    int size;

    // ware_add item to any position
    void (*ware_curtain_add) (struct curtain_linked_list*, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID, int);
    // ware_aircond_add item after tail
    void (*ware_curtain_addLast) (struct curtain_linked_list*, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID);
    // ware_aircond_add item before head
    void (*ware_curtain_addFirst) (struct curtain_linked_list*, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID);

    // get item from any position
    node_curtain * (*ware_curtain_get) (struct curtain_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // remove item from any position
    void (*ware_curtain_remove) (struct curtain_linked_list*, WARE_DEV ware_dev, u8 *devUnitID);

    // display all element in the LinkedList
    void (*ware_curtain_display) (struct curtain_linked_list*);

    // create a node with item
    node_curtain* (*ware_curtain_createNode) (WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID);
} curtain_linked_list;

/** ware_add item to any position
 */
void ware_curtain_add(curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID, int position);
/** ware_add item to head
 */
void ware_curtain_addFirst (curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID);
/** ware_add item to tail
 */
void ware_curtain_addLast (curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN curtain, u8 *devUnitID);

/** get item from specific position
 */
node_curtain *ware_curtain_get(curtain_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** get item and remove it from any position
 */
void ware_curtain_remove (curtain_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID);
/** display the items in the list
 */
void ware_curtain_display (curtain_linked_list* _this);
/** create a LinkedList
 */
curtain_linked_list ware_curtain_create_linked_list ();

typedef struct NodeScene {
    u8 devUnitID[12];
    SCENE_EVENT scene;
    // previous node
    struct NodeScene* prev;
    // next node
    struct NodeScene* next;
} node_scene;


typedef struct scene_linked_list {
    node_scene* head;
    node_scene* tail;
    // size of this LinkedList
    int size;

    // ware_add item to any position
    void (*ware_scene_add) (struct scene_linked_list*,  SCENE_EVENT scene, u8 *devUnitID, int);
    // ware_aircond_add item after tail
    void (*ware_scene_addLast) (struct scene_linked_list*,  SCENE_EVENT scene, u8 *devUnitID);
    // ware_aircond_add item before head
    void (*ware_scene_addFirst) (struct scene_linked_list*,  SCENE_EVENT scene, u8 *devUnitID);

    // get item from any position
    node_scene * (*ware_scene_get) (struct scene_linked_list*,  SCENE_EVENT scene, u8 *devUnitID);

    // remove item from any position
    void (*ware_scene_remove) (struct scene_linked_list*,  SCENE_EVENT scene, u8 *devUnitID);

    // display all element in the LinkedList
    void (*ware_scene_display) (struct scene_linked_list*);

    // create a node with item
    node_scene* (*ware_scene_createNode) ( SCENE_EVENT scene, u8 *devUnitID);
} scene_linked_list;

/** ware_add item to any position
 */
void ware_scene_add(scene_linked_list* _this,  SCENE_EVENT scene, u8 *devUnitID, int position);
/** ware_add item to head
 */
void ware_scene_addFirst (scene_linked_list* _this,  SCENE_EVENT scene, u8 *devUnitID);
/** ware_add item to tail
 */
void ware_scene_addLast (scene_linked_list* _this,  SCENE_EVENT scene, u8 *devUnitID);

/** get item from specific position
 */
node_scene *ware_scene_get(scene_linked_list* _this, SCENE_EVENT scene, u8 *devUnitID);
/** get item and remove it from any position
 */
void ware_scene_remove (scene_linked_list* _this, SCENE_EVENT scene, u8 *devUnitID);

/** display the items in the list
 */
void ware_scene_display (scene_linked_list* _this);
/** create a LinkedList
 */
scene_linked_list ware_scene_create_linked_list ();

typedef struct node_board {
    u8 devUnitID[12];
    BOARD_CHNOUT board;
    // previous node
    struct node_board* prev;
    // next node
    struct node_board* next;
} node_board;


typedef struct board_linked_list {
    node_board* head;
    node_board* tail;
    // size of this LinkedList
    int size;

    // board_add item to any position
    void (*board_add) (struct board_linked_list*,  BOARD_CHNOUT board, u8 *devUnitID, int);
    // board_aircond_add item after tail
    void (*board_addLast) (struct board_linked_list*,  BOARD_CHNOUT board, u8 *devUnitID);
    // board_aircond_add item before head
    void (*board_addFirst) (struct board_linked_list*,  BOARD_CHNOUT board, u8 *devUnitID);

    // get item from any position
    node_board * (*board_get) (struct board_linked_list*,  BOARD_CHNOUT board, u8 *devUnitID);
    // remove item from any position
    void (*board_remove) (struct board_linked_list*,  BOARD_CHNOUT board, u8 *devUnitID);

    // display all element in the LinkedList
    void (*board_display) (struct board_linked_list*);

    // create a node with item
    node_board* (*board_create_node) (BOARD_CHNOUT board, u8 *devUnitID);
} board_linked_list;

/** board_add item to any position
 */
void board_add(board_linked_list* _this,  BOARD_CHNOUT board, u8 *devUnitID, int position);
/** board_add item to head
 */
void board_addFirst (board_linked_list* _this,  BOARD_CHNOUT board, u8 *devUnitID);
/** board_add item to tail
 */
void board_addLast (board_linked_list* _this,  BOARD_CHNOUT board, u8 *devUnitID);

/** get item from specific position
 */
node_board *board_get(board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID);
/** get item and remove it from any position
 */
void board_remove (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID);
/** display the items in the list
 */
void board_display (board_linked_list* _this);
/** create a LinkedList
 */
board_linked_list board_create_linked_list ();

typedef struct node_keyinput {
    u8 devUnitID[12];
    BOARD_KEYINPUT keyinput;
    // previous node
    struct node_keyinput* prev;
    // next node
    struct node_keyinput* next;
} node_keyinput;


typedef struct keyinput_linked_list {
    node_keyinput* head;
    node_keyinput* tail;
    // size of this LinkedList
    int size;

    // keyinput_add item to any position
    void (*keyinput_add) (struct keyinput_linked_list*,  BOARD_KEYINPUT keyinput, u8 *devUnitID, int);
    // keyinput_aircond_add item after tail
    void (*keyinput_addLast) (struct keyinput_linked_list*,  BOARD_KEYINPUT keyinput, u8 *devUnitID);
    // keyinput_aircond_add item before head
    void (*keyinput_addFirst) (struct keyinput_linked_list*,  BOARD_KEYINPUT keyinput, u8 *devUnitID);

    // get item from any position
    node_keyinput * (*keyinput_get) (struct keyinput_linked_list*,  BOARD_KEYINPUT keyinput, u8 *devUnitID);

    // remove item from any position
    void (*keyinput_remove) (struct keyinput_linked_list*,  BOARD_KEYINPUT keyinput, u8 *devUnitID);

    // display all element in the LinkedList
    void (*keyinput_display) (struct keyinput_linked_list*);

    // create a node with item
    node_keyinput* (*keyinput_create_node) ( BOARD_KEYINPUT keyinput, u8 *devUnitID);
} keyinput_linked_list;

/** keyinput_add item to any position
 */
void keyinput_add(keyinput_linked_list* _this,  BOARD_KEYINPUT keyinput, u8 *devUnitID, int position);
/** keyinput_add item to head
 */
void keyinput_addFirst (keyinput_linked_list* _this,  BOARD_KEYINPUT keyinput, u8 *devUnitID);
/** keyinput_add item to tail
 */
void keyinput_addLast (keyinput_linked_list* _this,  BOARD_KEYINPUT keyinput, u8 *devUnitID);

/** get item from specific position
 */
node_keyinput *keyinput_get(keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID);
/** get item and remove it from any position
 */
void keyinput_remove (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID);
/** display the items in the list
 */
void keyinput_display (keyinput_linked_list* _this);
/** create a LinkedList
 */
keyinput_linked_list keyinput_create_linked_list ();

typedef struct node_chnop_item {
    u8 devUnitID[12];
    u8 chn_board_id[12];
    int devType;
    int devID;
    int item_num;
    CHNOP_ITEM chnop_item;
    // previous node
    struct node_chnop_item* prev;
    // next node
    struct node_chnop_item* next;
} node_chnop_item;


typedef struct chnop_item_linked_list {
    node_chnop_item* head;
    node_chnop_item* tail;
    // size of this LinkedList
    int size;

    // chnop_item_add item to any position
    void (*chnop_item_add) (struct chnop_item_linked_list*,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num, int);
    // chnop_item_aircond_add item after tail
    void (*chnop_item_addLast) (struct chnop_item_linked_list*,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num);
    // chnop_item_aircond_add item before head
    void (*chnop_item_addFirst) (struct chnop_item_linked_list*,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num);

    // get item from any position
    node_chnop_item * (*chnop_item_get) (struct chnop_item_linked_list*,  CHNOP_ITEM chnop_item, u8 *devUnitID);

    // remove item from any position
    void (*chnop_item_remove) (struct chnop_item_linked_list*,  CHNOP_ITEM chnop_item, u8 *devUnitID);

    // display all element in the LinkedList
    void (*chnop_item_display) (struct chnop_item_linked_list*);

    // create a node with item
    node_chnop_item* (*chnop_item_create_node) ( CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num);
} chnop_item_linked_list;

/** chnop_item_add item to any position
 */
void chnop_item_add(chnop_item_linked_list* _this,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num, int position);
/** chnop_item_add item to head
 */
void chnop_item_addFirst (chnop_item_linked_list* _this,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num);
/** chnop_item_add item to tail
 */
void chnop_item_addLast (chnop_item_linked_list* _this,  CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num);

/** get item from specific position
 */
node_chnop_item *chnop_item_get(chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID);
/** get item and remove it from any position
 */
void chnop_item_remove (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID);
/** display the items in the list
 */
void chnop_item_display (chnop_item_linked_list* _this);
/** create a LinkedList
 */
chnop_item_linked_list chnop_item_create_linked_list ();

typedef struct node_keyop_item {
    u8 devUnitID[12];
    u8 keyinput_board_id[12];
    int key_index;
    KEYOP_ITEM keyop_item;
    // previous node
    struct node_keyop_item* prev;
    // next node
    struct node_keyop_item* next;
} node_keyop_item;


typedef struct keyop_item_linked_list {
    node_keyop_item* head;
    node_keyop_item* tail;
    // size of this LinkedList
    int size;

    // keyop_item_add item to any position
    void (*keyop_item_add) (struct keyop_item_linked_list*,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int key_index, int);
    // keyop_item_aircond_add item after tail
    void (*keyop_item_addLast) (struct keyop_item_linked_list*,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int key_index);
    // keyop_item_aircond_add item before head
    void (*keyop_item_addFirst) (struct keyop_item_linked_list*,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int key_index);

    // get item from any position
    node_keyop_item * (*keyop_item_get) (struct keyop_item_linked_list*, u8 *devUnitID, u8 *keyinput_board_id, int key_index);

    // remove item from any position
    void (*keyop_item_remove) (struct keyop_item_linked_list*, u8 *devUnitID, u8 *keyinput_board_id, int key_index);

    // display all element in the LinkedList
    void (*keyop_item_display) (struct keyop_item_linked_list*);

    // create a node with item
    node_keyop_item* (*keyop_item_create_node) ( KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index);
} keyop_item_linked_list;

/** keyop_item_add item to any position
 */
void keyop_item_add(keyop_item_linked_list* _this,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index, int position);
/** keyop_item_add item to head
 */
void keyop_item_addFirst (keyop_item_linked_list* _this,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index);
/** keyop_item_add item to tail
 */
void keyop_item_addLast (keyop_item_linked_list* _this,  KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index);

/** get item from specific position
 */
node_keyop_item *keyop_item_get(keyop_item_linked_list* _this, u8 *devUnitID, u8 *keyinput_board_id, int index);
/** get item and remove it from any position
 */
void keyop_item_remove (keyop_item_linked_list* _this, u8 *devUnitID, u8 *keyinput_board_id, int index);
/** display the items in the list
 */
void keyop_item_display (keyop_item_linked_list* _this);
/** create a LinkedList
 */
keyop_item_linked_list keyop_item_create_linked_list ();


typedef struct node_gw_client
{
    struct sockaddr_in    gw_sender;
    u8                    gw_id[12];
    u8                    gw_pass[8];
    u8                    rcu_ip[4];
    // previous node
    struct node_gw_client* prev;
    // next node
    struct node_gw_client* next;
} node_gw_client;

typedef struct gw_client_linked_list {
    node_gw_client* head;
    node_gw_client* tail;
    // size of this LinkedList
    int size;

    // keyop_item_add item to any position
    void (*gw_client_add) (struct gw_client_linked_list*,  struct sockaddr_in , u8 *, u8 *, u8 *,int);
    // keyop_item_aircond_add item after tail
    void (*gw_client_addLast) (struct gw_client_linked_list*,  struct sockaddr_in , u8 *, u8 *, u8 *);
    // keyop_item_aircond_add item before head
    void (*gw_client_addFirst) (struct gw_client_linked_list*,  struct sockaddr_in , u8 *, u8 *, u8 *);

    // get item from any position
    node_gw_client * (*gw_client_get) (struct gw_client_linked_list*, u8 *, u8 *);

    // remove item from any position
    void (*gw_client_remove) (struct gw_client_linked_list*,u8 *, u8 *);

    // display all element in the LinkedList
    void (*gw_client_display) (struct gw_client_linked_list*);

    // create a node with item
    node_gw_client* (*gw_client_create_node) (struct sockaddr_in , u8 *, u8 *, u8 *);
} gw_client_linked_list;


/** keyop_item_add item to any position
 */
void gw_client_add(gw_client_linked_list* _this,  struct sockaddr_in sender, u8 *gw_id, u8 *gw_pass, u8 *ip, int position);
/** keyop_item_add item to head
 */
void gw_client_addLast (gw_client_linked_list* _this,  struct sockaddr_in sender, u8 *gw_id, u8 *gw_pass, u8 *ip);
/** keyop_item_add item to tail
 */
void gw_client_addFirst (gw_client_linked_list* _this,  struct sockaddr_in sender, u8 *gw_id, u8 *gw_pass, u8 *ip);

/** get item from specific position
 */
node_gw_client **gw_client_get(gw_client_linked_list* _this,u8 *gw_id, u8 *gw_pass);
/** get item and remove it from any position
 */
void gw_client_remove (gw_client_linked_list* _this,u8 *gw_id, u8 *gw_pass);
/** display the items in the list
 */
void gw_client_display (gw_client_linked_list* _this);
/** create a LinkedList
 */
gw_client_linked_list gw_client_create_linked_list ();

typedef struct node_app_client
{
    struct sockaddr_in    app_sender;
    u8                    app_ip[4];
    // previous node
    struct node_app_client* prev;
    // next node
    struct node_app_client* next;
} node_app_client;

typedef struct app_client_linked_list {
    node_app_client* head;
    node_app_client* tail;
    // size of this LinkedList
    int size;

    // keyop_item_add item to any position
    void (*app_client_add) (struct app_client_linked_list*,  struct sockaddr_in , u8 *, int);
    // keyop_item_aircond_add item after tail
    void (*app_client_addLast) (struct app_client_linked_list*,  struct sockaddr_in, u8 * );
    // keyop_item_aircond_add item before head
    void (*app_client_addFirst) (struct app_client_linked_list*,  struct sockaddr_in, u8 * );

    // get item from any position
    node_app_client * (*app_client_get) (struct app_client_linked_list*);

    // remove item from any position
    void (*app_client_remove) (struct app_client_linked_list*, struct sockaddr_in);

    // display all element in the LinkedList
    void (*app_client_display) (struct app_client_linked_list*);

    // create a node with item
    node_app_client* (*app_client_create_node) (struct sockaddr_in, u8 *app_ip);
} app_client_linked_list;


/** keyop_item_add item to any position
 */
void app_client_add(app_client_linked_list* _this,  struct sockaddr_in sender, u8 *app_ip, int position);
/** keyop_item_add item to head
 */
void app_client_addLast (app_client_linked_list* _this,  struct sockaddr_in sender, u8 *app_ip);
/** keyop_item_add item to tail
 */
void app_client_addFirst (app_client_linked_list* _this,  struct sockaddr_in sender, u8 *app_ip);

/** get item from specific position
 */
node_app_client **app_client_get(app_client_linked_list* _this);
/** get item and remove it from any position
 */
void app_client_remove (app_client_linked_list* _this, struct sockaddr_in sender);
/** display the items in the list
 */
void app_client_display (app_client_linked_list* _this);
/** create a LinkedList
 */
app_client_linked_list app_client_create_linked_list ();

typedef struct node_udp_msg_queue
{
    u8 devUnitID[12];
    int cmd;
    int id;
    int flag;
    // previous node
    struct node_udp_msg_queue* prev;
    // next node
    struct node_udp_msg_queue* next;
} node_udp_msg_queue;

typedef struct udp_msg_queue_linked_list {
    node_udp_msg_queue* head;
    node_udp_msg_queue* tail;
    // size of this LinkedList
    int size;

    // keyop_item_add item to any position
    void (*udp_msg_queue_add) (struct udp_msg_queue_linked_list*, u8 *, int ,int, int, int);
    // keyop_item_aircond_add item after tail
    void (*udp_msg_queue_addLast) (struct udp_msg_queue_linked_list*, u8 *, int ,int, int);
    // keyop_item_aircond_add item before head
    void (*udp_msg_queue_addFirst) (struct udp_msg_queue_linked_list*, u8 *, int ,int, int);
    /** get item from specific position
     */
    int (*udp_msg_queue_get)(struct udp_msg_queue_linked_list* _this, int flag);
    // remove item from any position
    void (*udp_msg_queue_remove) (struct udp_msg_queue_linked_list*,u8 *, int);

    // display all element in the LinkedList
    void (*udp_msg_queue_display) (struct udp_msg_queue_linked_list*);

    // create a node with item
    node_udp_msg_queue* (*udp_msg_queue_create_node) (u8 *, int ,int, int);
} udp_msg_queue_linked_list;


/** keyop_item_add item to any position
 */
void udp_msg_queue_add(udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd, int id, int flag, int position);
/** keyop_item_add item to head
 */
void udp_msg_queue_addLast (udp_msg_queue_linked_list* _this,  u8 *devUnitID, int cmd, int id, int flag);
/** keyop_item_add item to tail
 */
void udp_msg_queue_addFirst (udp_msg_queue_linked_list* _this,  u8 *devUnitID, int cmd, int id, int flag);
/** get item from specific position
 */
int udp_msg_queue_get(udp_msg_queue_linked_list* _this, int flag);

/** get item and remove it from any position
 */
void udp_msg_queue_remove (udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd);
/** display the items in the list
 */
void udp_msg_queue_display (udp_msg_queue_linked_list* _this);
/** create a LinkedList
 */
udp_msg_queue_linked_list udp_msg_queue_create_linked_list ();

#endif // LINK_LIST_H
