#include <stdio.h>
#include <stdarg.h>
#include <stdlib.h>
#include <string.h>

#include "link_list.h"
#include "debug.h"

/** rcu_add item to any position
 */
void rcu_add (rcu_linked_list* _this, RCU_INFO item, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("rcu_linked_list#rcu_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // rcu_add to head
    if (position == 0) {
        _this->rcu_addFirst(_this, item);
    } else if (position == _this->size) {
        // rcu_add to tail
        _this->rcu_addLast(_this, item);
    }
}
/** rcu_add item to head
 */
void rcu_addFirst (rcu_linked_list* _this, RCU_INFO item)
{
    node_rcu* newNode = _this->rcu_create_node(item);
    node_rcu* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_rcu* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        while(head) {

            if(memcmp(head->item.devUnitID, item.devUnitID, 12) == 0) {
                head->item = item;
                return;
            }
            head = head->next;
        }
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** rcu_add item to tail
 */
void rcu_addLast (rcu_linked_list* _this, RCU_INFO item)
{
    node_rcu* newNode = _this->rcu_create_node(item);
    node_rcu* head = _this->head;
    node_rcu* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_rcu* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if(memcmp(head->item.devUnitID, item.devUnitID, 12) == 0) {
                head->item = item;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** rcu_get item from specific position
 */
RCU_INFO rcu_get (rcu_linked_list* _this, u8 *devUnitID, u8* devUnitPass)
{

    RCU_INFO rcu_info;
    memset(&rcu_info, 0, sizeof(RCU_INFO));

    node_rcu* node = _this->head;
    int i = 0;
    // loop until position
    while (i < _this->size && memcmp(node->item.devUnitID, devUnitID, 12) != 0) {
        node = node->next;
        i++;
    }
    if(node != NULL) {
        memcpy(node->item.devUnitPass, devUnitPass, 8);
        return node->item;
    } else
        return rcu_info;
}

/** rcu_get item and rcu_remove it
 */
void rcu_remove (rcu_linked_list* _this, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("rcu_linked_list#_rcu_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }

    node_rcu* node = _this->head;
    node_rcu* prev;
    node_rcu* next;

    // loop until position
    while (node) {
        if (memcmp(node->item.devUnitID, devUnitID, 12) == 0) {
            // rcu_remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;
        } else {
            node = node->next;
        }
    }

}


/** rcu_display the items in the list
 */
void rcu_display (rcu_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        //LOGI("has %d items\n", size);
        u8 str[25] = {0};
        node_rcu* node = _this->head;
        for (i = 0; i < size; i++) {

            bytes_to_string (node->item.devUnitID, str , 12);
            LOGI("联网模块名称：%s  设备ID：%s\n", node->item.name, str);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a node_rcu
 */
node_rcu* rcu_create_node (RCU_INFO item)
{
    node_rcu* node = (node_rcu*) malloc (sizeof(node_rcu));
    node->item = item;
    node->prev = NULL;
    node->next = NULL;
    return node;
}

/** create a rcu_linked_list
 */
rcu_linked_list rcu_create_linked_list ()
{
    rcu_linked_list list;

    list.head = NULL;
    list.tail = NULL;
    list.rcu_add = &rcu_add;
    list.rcu_addFirst = &rcu_addFirst;
    list.rcu_addLast = &rcu_addLast;
    list.rcu_get = &rcu_get;
    list.rcu_remove = &rcu_remove;
    list.rcu_display = &rcu_display;
    list.rcu_create_node = &rcu_create_node;
    list.size = 0;

    return list;
}

/** ware_add item to any position
 */
void ware_add (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("ware_linked_list#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->ware_addFirst(_this, ware_dev, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->ware_addLast(_this, ware_dev, devUnitID);
    }
}
/** ware_add item to head
 */
void ware_addFirst (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    Node* newNode = _this->ware_create_node(ware_dev, devUnitID);
    Node* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void ware_addLast (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    Node* newNode = _this->ware_create_node(ware_dev, devUnitID);
    Node* head = _this->head;
    Node* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if( memcmp(head->devUnitID, devUnitID, 12) == 0
                && memcmp(head->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
                && head->ware_dev.devId == ware_dev.devId
                && head->ware_dev.devType == ware_dev.devType) {
                head->ware_dev = ware_dev;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** ware_get item from specific position
 */
WARE_DEV ware_get (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("ware_linked_list#ware_get: The list is empty.");
        system("PAUSE");
        exit(0);
    }

    Node* node = _this->head;
    int i = 0;

    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           || memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) != 0
           || node->ware_dev.devId != ware_dev.devId
           || node->ware_dev.devType != ware_dev.devType) {
        node = node->next;
        i++;
    }

    return node->ware_dev;

}

/** ware_get item and remove it from any position
 */
void ware_remove (ware_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("ware_linked_list#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    Node* node = _this->head;
    Node* prev;
    Node* next;

    // loop until position
    while (node) {
        if( memcmp(node->devUnitID, devUnitID, 12) != 0
            && memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) != 0
            && node->ware_dev.devId != ware_dev.devId
            && node->ware_dev.devType != ware_dev.devType) {

            // remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            }
            free(node);
            _this->size--;

        } else {
            node = node->next;
        }
    }
}

/** ware_display the items in the list
 */
void ware_display (ware_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("设备 has %d items\n", size);
        Node* node = _this->head;
        for (i = 0; i < size; i++) {

            LOGI("设备类型：%d  设备ID：%d\n", node->ware_dev.devType, node->ware_dev.devId);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
Node* ware_create_node (WARE_DEV item, u8 *devUnitID)
{
    Node* node = (Node*) malloc (sizeof(Node));
    memcpy(node->devUnitID, devUnitID, 12);
    node->ware_dev = item;
    node->prev = NULL;
    node->next = NULL;
    return node;
}
/** create a ware_linked_list
 */
ware_linked_list ware_create_linked_list ()
{
    ware_linked_list list;

    list.head = NULL;
    list.tail = NULL;
    list.ware_add = &ware_add;
    list.ware_addFirst = &ware_addFirst;
    list.ware_addLast = &ware_addLast;
    list.ware_get = &ware_get;
    list.ware_remove = &ware_remove;
    list.ware_display = &ware_display;
    list.ware_create_node = &ware_create_node;
    list.size = 0;

    return list;
}


/** ware_aircond_add item to any position
 */
void ware_aircond_add (aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }

    // ware_add to head
    if (position == 0) {
        _this->ware_aircond_addFirst(_this, ware_dev, aircond, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->ware_aircond_addLast(_this, ware_dev, aircond, devUnitID);
    }
}

/** ware_aircond_add item to head
 */
void ware_aircond_addFirst (aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID)
{
    Node_aircond* newNode = _this->ware_aircond_createNode(ware_dev, aircond, devUnitID);
    Node_aircond* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node_aircond* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void ware_aircond_addLast (aircond_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_AIRCOND aircond, u8 *devUnitID)
{
    Node_aircond* newNode = _this->ware_aircond_createNode(ware_dev, aircond, devUnitID);
    Node_aircond* head = _this->head;
    Node_aircond* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node_aircond* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && memcmp(head->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
               && head->ware_dev.devId == ware_dev.devId
               && head->ware_dev.devType == ware_dev.devType) {
                head->ware_dev = ware_dev;
                head->aircond = aircond;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** get item and remove it from any position
 */
void ware_aircond_remove (aircond_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    Node_aircond* node = _this->head;
    Node_aircond* prev;
    Node_aircond* next;

    // loop until position
    while(node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
           && node->ware_dev.devId == ware_dev.devId
           && node->ware_dev.devType == ware_dev.devType) {

            // remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            }
            free(node);
            _this->size--;
        }
        node = node->next;
    }

}
/** display the items in the list
 */
void ware_aircond_display (aircond_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("空调 has %d items\n", size);
        Node_aircond* node = _this->head;
        for (i = 0; i < size; i++) {
            LOGI("空调设备类型：%d  设备ID：%d, 设备状态:%d", node->ware_dev.devType, node->ware_dev.devId, node->aircond.bOnOff);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
Node_aircond* ware_aircond_createNode (WARE_DEV ware_dev, DEV_PRO_AIRCOND ware_aircond, u8 *devUnitID )
{
    Node_aircond* node = (Node_aircond*) malloc (sizeof(Node_aircond));
    memcpy(node->devUnitID, devUnitID, 12);
    node->ware_dev = ware_dev;
    node->aircond = ware_aircond;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
aircond_linked_list ware_aircond_create_linked_list ()
{
    aircond_linked_list ware_aircond_list;

    ware_aircond_list.head = NULL;
    ware_aircond_list.tail = NULL;
    ware_aircond_list.ware_aircond_add = &ware_aircond_add;
    ware_aircond_list.ware_aircond_addFirst = &ware_aircond_addFirst;
    ware_aircond_list.ware_aircond_addLast = &ware_aircond_addLast;
    ware_aircond_list.ware_aircond_remove = &ware_aircond_remove;
    ware_aircond_list.ware_aircond_display = &ware_aircond_display;
    ware_aircond_list.ware_aircond_createNode = &ware_aircond_createNode;

    ware_aircond_list.size = 0;

    return ware_aircond_list;
}

/** ware_ware_light_add item to any position
 */
void ware_light_add (light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT ware_light, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->ware_light_addFirst(_this, ware_dev, ware_light, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->ware_light_addLast(_this, ware_dev, ware_light, devUnitID);
    }
}

/** ware_ware_light_add item to head
 */
void ware_light_addFirst (light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT ware_light, u8 *devUnitID)
{
    Node_light* newNode = _this->ware_light_createNode(ware_dev, ware_light, devUnitID);
    Node_light* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node_light* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void ware_light_addLast (light_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_LIGHT ware_light, u8 *devUnitID)
{
    Node_light* newNode = _this->ware_light_createNode(ware_dev, ware_light, devUnitID);
    Node_light* head = _this->head;
    Node_light* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        Node_light* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if( memcmp(head->devUnitID, devUnitID, 12) == 0
                && memcmp(head->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
                && head->ware_dev.devId == ware_dev.devId
                && head->ware_dev.devType == ware_dev.devType) {
                head->ware_dev = ware_dev;
                head->light.bOnOff = ware_light.bOnOff;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
Node_light *ware_light_get (light_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    Node_light* node = _this->head;
    int i = 0;
    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           || memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) != 0
           || node->ware_dev.devId != ware_dev.devId
           || node->ware_dev.devType != ware_dev.devType) {
        node = node->next;
        i++;
    }
    return node;

}

/** get item and remove it from any position
 */
void ware_light_remove (light_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    Node_light* node = _this->head;
    Node_light* prev;
    Node_light* next;

    while(node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
           && node->ware_dev.devId == ware_dev.devId
           && node->ware_dev.devType == ware_dev.devType) {

            // remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            }
            free(node);
            _this->size--;
        }
        node = node->next;
    }

}
/** display the items in the list
 */
void ware_light_display (light_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("灯光 has %d items\n", size);
        Node_light* node = _this->head;
        for (i = 0; i < size; i++) {

            LOGI("灯光设备类型：%d  设备ID：%d 设备状态:%d\n", node->ware_dev.devType, node->ware_dev.devId, node->light.bOnOff);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
Node_light* ware_light_createNode (WARE_DEV ware_dev, DEV_PRO_LIGHT ware_light, u8 *devUnitID)
{
    Node_light* node = (Node_light*) malloc (sizeof(Node_light));
    memcpy(node->devUnitID, devUnitID, 12);
    node->ware_dev = ware_dev;
    node->light = ware_light;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
light_linked_list ware_light_create_linked_list ()
{
    light_linked_list ware_light_list;

    ware_light_list.head = NULL;
    ware_light_list.tail = NULL;
    ware_light_list.ware_light_add = &ware_light_add;
    ware_light_list.ware_light_addFirst = &ware_light_addFirst;
    ware_light_list.ware_light_addLast = &ware_light_addLast;
    ware_light_list.ware_light_get = &ware_light_get;
    ware_light_list.ware_light_remove = &ware_light_remove;
    ware_light_list.ware_light_display = &ware_light_display;
    ware_light_list.ware_light_createNode = &ware_light_createNode;
    ware_light_list.size = 0;

    return ware_light_list;
}



/** ware_ware_curtain_add item to any position
 */
void ware_curtain_add (curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN ware_curtain, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->ware_curtain_addFirst(_this, ware_dev, ware_curtain, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->ware_curtain_addLast(_this, ware_dev, ware_curtain, devUnitID);
    }
}

/** ware_ware_curtain_add item to head
 */
void ware_curtain_addFirst (curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN ware_curtain, u8 *devUnitID)
{
    node_curtain* newNode = _this->ware_curtain_createNode(ware_dev, ware_curtain, devUnitID);
    node_curtain* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_curtain* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void ware_curtain_addLast (curtain_linked_list* _this, WARE_DEV ware_dev, DEV_PRO_CURTAIN ware_curtain, u8 *devUnitID)
{
    node_curtain* newNode = _this->ware_curtain_createNode(ware_dev, ware_curtain, devUnitID);
    node_curtain* head = _this->head;
    node_curtain* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_curtain* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if( memcmp(head->devUnitID, devUnitID, 12) == 0
                && memcmp(head->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
                && head->ware_dev.devId == ware_dev.devId
                && head->ware_dev.devType == ware_dev.devType) {
                head->ware_dev = ware_dev;
                head->curtain = ware_curtain;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
node_curtain *ware_curtain_get (curtain_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_curtain* node = _this->head;
    int i = 0;
    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           || memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) != 0
           || node->ware_dev.devId != ware_dev.devId
           || node->ware_dev.devType != ware_dev.devType) {
        node = node->next;
        i++;
    }
    return node;

}

/** get item and remove it from any position
 */
void ware_curtain_remove (curtain_linked_list* _this, WARE_DEV ware_dev, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_curtain* node = _this->head;
    node_curtain* prev;
    node_curtain* next;

    // loop until position
    while(node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && memcmp(node->ware_dev.canCpuId, ware_dev.canCpuId, 12) == 0
           && node->ware_dev.devId == ware_dev.devId
           && node->ware_dev.devType == ware_dev.devType) {

            // remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            }
            free(node);
            _this->size--;
        }
        node = node->next;
    }


}
/** display the items in the list
 */
void ware_curtain_display (curtain_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("窗帘 has %d items\n", size);
        node_curtain* node = _this->head;
        for (i = 0; i < size; i++) {

            LOGI("窗帘设备类型：%d  设备ID：%d\n", node->ware_dev.devType, node->ware_dev.devId);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_curtain* ware_curtain_createNode (WARE_DEV ware_dev, DEV_PRO_CURTAIN ware_curtain, u8 *devUnitID)
{
    node_curtain* node = (node_curtain*) malloc (sizeof(node_curtain));
    memcpy(node->devUnitID, devUnitID, 12);
    node->ware_dev = ware_dev;
    node->curtain = ware_curtain;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
curtain_linked_list ware_curtain_create_linked_list ()
{
    curtain_linked_list ware_curtain_list;

    ware_curtain_list.head = NULL;
    ware_curtain_list.tail = NULL;
    ware_curtain_list.ware_curtain_add = &ware_curtain_add;
    ware_curtain_list.ware_curtain_addFirst = &ware_curtain_addFirst;
    ware_curtain_list.ware_curtain_addLast = &ware_curtain_addLast;
    ware_curtain_list.ware_curtain_get = &ware_curtain_get;
    ware_curtain_list.ware_curtain_remove = &ware_curtain_remove;
    ware_curtain_list.ware_curtain_display = &ware_curtain_display;
    ware_curtain_list.ware_curtain_createNode = &ware_curtain_createNode;
    ware_curtain_list.size = 0;

    return ware_curtain_list;
}

/** ware_scene_add item to any position
 */
void ware_scene_add (scene_linked_list* _this, SCENE_EVENT scene, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->ware_scene_addFirst(_this, scene, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->ware_scene_addLast(_this, scene, devUnitID);
    }
}

/** ware_scene_add item to head
 */
void ware_scene_addFirst (scene_linked_list* _this, SCENE_EVENT scene, u8 *devUnitID)
{
    node_scene* newNode = _this->ware_scene_createNode(scene, devUnitID);
    node_scene* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_scene* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_sense item to tail
 */
void ware_scene_addLast (scene_linked_list* _this,  SCENE_EVENT scene, u8 *devUnitID)
{
    node_scene* newNode = _this->ware_scene_createNode(scene, devUnitID);
    node_scene* head = _this->head;
    node_scene* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_scene* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && head->scene.eventId == scene.eventId) {
                head->scene = scene;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

        LOGI("scene_list size = %d\n", _this->size);
    }
}


/** get item from specific position
 */
node_scene *ware_scene_get (scene_linked_list* _this, SCENE_EVENT scene, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_scene* node = _this->head;
    int i = 0;
    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           || node->scene.eventId != scene.eventId) {
        node = node->next;
        i++;
    }
    return node;

}

/** get item and remove it from any position
 */
void ware_scene_remove (scene_linked_list* _this, SCENE_EVENT scene_event, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_scene* node = _this->head;
    node_scene* prev;
    node_scene* next;

    // loop until position
    while (node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && node->scene.eventId == scene_event.eventId) {

            // remove node from list
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;;
        }
        node = node->next;
    }


}
/** display the items in the list
 */
void ware_scene_display (scene_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("情景模式 has %d items\n", size);
        node_scene* node = _this->head;
        for (i = 0; i < size; i++) {
            LOGI("情景模式名称: %s 情景模式ID：%d\n", node->scene.sceneName, node->scene.eventId);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_scene* ware_scene_createNode (SCENE_EVENT scene, u8 *devUnitID)
{
    node_scene* node = (node_scene*) malloc (sizeof(node_scene));
    memcpy(node->devUnitID, devUnitID, 12);
    node->scene = scene;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
scene_linked_list ware_scene_create_linked_list ()
{
    scene_linked_list ware_scene_list;

    ware_scene_list.head = NULL;
    ware_scene_list.tail = NULL;
    ware_scene_list.ware_scene_add = &ware_scene_add;
    ware_scene_list.ware_scene_addFirst = &ware_scene_addFirst;
    ware_scene_list.ware_scene_addLast = &ware_scene_addLast;
    ware_scene_list.ware_scene_get = &ware_scene_get;
    ware_scene_list.ware_scene_remove = &ware_scene_remove;

    ware_scene_list.ware_scene_display = &ware_scene_display;
    ware_scene_list.ware_scene_createNode = &ware_scene_createNode;
    ware_scene_list.size = 0;

    return ware_scene_list;
}


/** board_add item to any position
 */
void board_add (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->board_addFirst(_this, board, devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->board_addLast(_this, board, devUnitID);
    }
}

/** board_add item to head
 */
void board_addFirst (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID)
{
    node_board* newNode = _this->board_create_node(board, devUnitID);
    node_board* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_board* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void board_addLast (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID)
{
    node_board* newNode = _this->board_create_node(board, devUnitID);
    node_board* head = _this->head;
    node_board* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_board* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && memcmp(head->board.devUnitID, board.devUnitID, 12) == 0) {
                head->board = board;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
node_board *board_get (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_board* node = _this->head;
    int i = 0;
    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           || memcmp(node->board.devUnitID, board.devUnitID, 12) != 0) {
        node = node->next;
        i++;
    }
    return node;
}

/** get item and remove it from any position
 */
void board_remove (board_linked_list* _this, BOARD_CHNOUT board, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_board* node = _this->head;
    node_board* prev;
    node_board* next;

    // loop until position
    while (node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && memcmp(node->board.devUnitID, board.devUnitID, 12) == 0) {
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;;
        }
        node = node->next;
    }
}
/** display the items in the list
 */
void board_display (board_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("输出板 has %d items\n", size);
        node_board* node = _this->head;
        u8 str[25] = {0};

        for (i = 0; i < size; i++) {
            bytes_to_string (node->board.devUnitID, str, 12);
            LOGI("输出板ID：%s\n", str);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_board* board_create_node (BOARD_CHNOUT board, u8 *devUnitID)
{
    node_board* node = (node_board*) malloc (sizeof(node_board));
    memcpy(node->devUnitID, devUnitID, 12);
    node->board = board;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
board_linked_list board_create_linked_list ()
{
    board_linked_list board_list;

    board_list.head = NULL;
    board_list.tail = NULL;
    board_list.board_add = &board_add;
    board_list.board_addFirst = &board_addFirst;
    board_list.board_addLast = &board_addLast;
    board_list.board_get = &board_get;
    board_list.board_remove = &board_remove;
    board_list.board_display = &board_display;
    board_list.board_create_node = &board_create_node;
    board_list.size = 0;

    return board_list;
}



/** keyinput_add item to any position
 */
void keyinput_add (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->keyinput_addFirst(_this, keyinput ,devUnitID);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->keyinput_addLast(_this, keyinput, devUnitID);
    }
}

/** keyinput_add item to head
 */
void keyinput_addFirst (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID)
{
    node_keyinput* newNode = _this->keyinput_create_node(keyinput, devUnitID);
    node_keyinput* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_keyinput* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void keyinput_addLast (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID)
{
    node_keyinput* newNode = _this->keyinput_create_node(keyinput, devUnitID);
    node_keyinput* head = _this->head;
    node_keyinput* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_keyinput* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && memcmp(head->keyinput.devUnitID, keyinput.devUnitID, 12) == 0) {
                head->keyinput = keyinput;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
node_keyinput *keyinput_get (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_keyinput* node = _this->head;
    int i = 0;
    // loop until position
    while (memcmp(node->devUnitID, devUnitID, 12) != 0
           ||memcmp(node->keyinput.devUnitID, keyinput.devUnitID, 12) != 0) {
        node = node->next;
        i++;
    }
    return node;

}

/** get item and remove it from any position
 */
void keyinput_remove (keyinput_linked_list* _this, BOARD_KEYINPUT keyinput, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_keyinput* node = _this->head;
    node_keyinput* prev;
    node_keyinput* next;

    // loop until position
    while (node) {
        if(memcmp(node->devUnitID, devUnitID, 12) == 0
           && memcmp(node->keyinput.devUnitID, keyinput.devUnitID, 12) == 0) {
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;;
        }
        node = node->next;
    }
}
/** display the items in the list
 */
void keyinput_display (keyinput_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        u8 uid[25] = {0};
        LOGI("输入板 has %d items\n", size);
        node_keyinput* node = _this->head;
        for (i = 0; i < size; i++) {
            bytes_to_string (node->keyinput.devUnitID, uid, 12);
            LOGI("输入板uid：%s\n", uid);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_keyinput* keyinput_create_node (BOARD_KEYINPUT keyinput, u8 *devUnitID)
{
    node_keyinput* node = (node_keyinput*) malloc (sizeof(node_keyinput));
    memcpy(node->devUnitID, devUnitID, 12);
    node->keyinput = keyinput;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
keyinput_linked_list keyinput_create_linked_list ()
{
    keyinput_linked_list keyinput_list;

    keyinput_list.head = NULL;
    keyinput_list.tail = NULL;
    keyinput_list.keyinput_add = &keyinput_add;
    keyinput_list.keyinput_addFirst = &keyinput_addFirst;
    keyinput_list.keyinput_addLast = &keyinput_addLast;
    keyinput_list.keyinput_get = &keyinput_get;
    keyinput_list.keyinput_remove = &keyinput_remove;
    keyinput_list.keyinput_display = &keyinput_display;
    keyinput_list.keyinput_create_node = &keyinput_create_node;
    keyinput_list.size = 0;

    return keyinput_list;
}



/** chnop_item_add item to any position
 */
void chnop_item_add (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num,int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->chnop_item_addFirst(_this, chnop_item,devUnitID, board_id, devType, devID, num);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->chnop_item_addLast(_this, chnop_item,devUnitID, board_id, devType, devID, num);
    }
}

/** chnop_item_add item to head
 */
void chnop_item_addFirst (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num)
{
    node_chnop_item* newNode = _this->chnop_item_create_node(chnop_item, devUnitID, board_id, devType, devID, num);
    node_chnop_item* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_chnop_item* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void chnop_item_addLast (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID,  u8 *board_id, int devType, int devID, int num)
{
    node_chnop_item* newNode = _this->chnop_item_create_node(chnop_item, devUnitID, board_id, devType, devID, num);
    node_chnop_item* head = _this->head;
    node_chnop_item* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_chnop_item* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if( memcmp(head->devUnitID, devUnitID, 12) == 0
                && memcmp(head->chnop_item.devUnitID , chnop_item.devUnitID, 12) == 0) {
                head->chnop_item = chnop_item;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
node_chnop_item *chnop_item_get (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_chnop_item* node = _this->head;
    int i = 0;
    // loop until position
    while ( memcmp(node->devUnitID, devUnitID, 12) != 0
            || memcmp(node->chnop_item.devUnitID, chnop_item.devUnitID, 12) != 0) {
        node = node->next;
        i++;
    }
    return node;

}

/** get item and remove it from any position
 */
void chnop_item_remove (chnop_item_linked_list* _this, CHNOP_ITEM chnop_item, u8 *devUnitID)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_chnop_item* node = _this->head;
    node_chnop_item* prev;
    node_chnop_item* next;

    // loop until position
    while (node) {
        if (memcmp(node->devUnitID, devUnitID, 12) != 0
            ||memcmp(node->chnop_item.devUnitID, chnop_item.devUnitID, 12) != 0) {
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;;
        }
        node = node->next;
    }
}
/** display the items in the list
 */
void chnop_item_display (chnop_item_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("输出按键 has %d items\n", size);
        u8 uid[25] = {0};
        node_chnop_item* node = _this->head;
        for (i = 0; i < size; i++) {
            bytes_to_string (node->chnop_item.devUnitID, uid, 12);

            LOGI("输出按键ID：%s\n", uid);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_chnop_item* chnop_item_create_node (CHNOP_ITEM chnop_item, u8 *devUnitID, u8 *board_id, int devType, int devID, int num)
{
    node_chnop_item* node = (node_chnop_item*) malloc (sizeof(node_chnop_item));
    memcpy(node->devUnitID, devUnitID, 12);
    memcpy(node->chn_board_id, board_id, 12);
    node->devType = devType;
    node->devID = devID;
    node->item_num = num;
    node->chnop_item = chnop_item;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
chnop_item_linked_list chnop_item_create_linked_list ()
{
    chnop_item_linked_list chnop_item_list;

    chnop_item_list.head = NULL;
    chnop_item_list.tail = NULL;
    chnop_item_list.chnop_item_add = &chnop_item_add;
    chnop_item_list.chnop_item_addFirst = &chnop_item_addFirst;
    chnop_item_list.chnop_item_addLast = &chnop_item_addLast;
    chnop_item_list.chnop_item_get = &chnop_item_get;
    chnop_item_list.chnop_item_remove = &chnop_item_remove;
    chnop_item_list.chnop_item_display = &chnop_item_display;
    chnop_item_list.chnop_item_create_node = &chnop_item_create_node;
    chnop_item_list.size = 0;

    return chnop_item_list;
}

/** keyop_item_add item to any position
 */
void keyop_item_add (keyop_item_linked_list* _this, KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->keyop_item_addFirst(_this, keyop_item, devUnitID, keyinput_board_id, index);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->keyop_item_addLast(_this, keyop_item, devUnitID, keyinput_board_id, index);
    }
}

/** keyop_item_add item to head
 */
void keyop_item_addFirst (keyop_item_linked_list* _this, KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index)
{
    node_keyop_item* newNode = _this->keyop_item_create_node(keyop_item, devUnitID, keyinput_board_id, index);
    node_keyop_item* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_keyop_item* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void keyop_item_addLast (keyop_item_linked_list* _this, KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index)
{
    node_keyop_item* newNode = _this->keyop_item_create_node(keyop_item, devUnitID, keyinput_board_id, index);
    node_keyop_item* head = _this->head;
    node_keyop_item* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_keyop_item* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {

            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && memcmp(head->keyinput_board_id, keyinput_board_id, 12) == 0
               && head->key_index == index) {
                head->keyop_item = keyop_item;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}


/** get item from specific position
 */
node_keyop_item *keyop_item_get (keyop_item_linked_list* _this, u8 *devUnitID, u8 *keyinput_board_id, int index)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_keyop_item* node = _this->head;
    int i = 0;
    // loop until position
    while (memcpy(node->devUnitID, devUnitID, 12) != 0
           || memcmp(node->keyinput_board_id, keyinput_board_id, 12) != 0
           || node->key_index != index) {
        node = node->next;
        i++;
    }
    return node;

}


/** get item and remove it from any position
 */
void keyop_item_remove (keyop_item_linked_list* _this, u8 *devUnitID, u8 *keyinput_board_id, int index)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_keyop_item* node = _this->head;
    node_keyop_item* prev;
    node_keyop_item* next;

    // loop until position
    while (node) {
        if( memcpy(node->devUnitID, devUnitID, 12) == 0
            && memcmp(node->keyinput_board_id, keyinput_board_id, 12) == 0
            && node->key_index == index) {
            prev = node->prev;
            next = node->next;
            prev->next = next;
            if(next != NULL) {
                next->prev = prev;
                _this->tail = next;
            } else {
                _this->tail = prev;
            }
            free(node);
            _this->size--;;
        }
        node = node->next;
    }
}

/** display the items in the list
 */
void keyop_item_display (keyop_item_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        node_keyop_item* node = _this->head;
        for (i = 0; i < size; i++) {
            if (i > 0)
                LOGI(", ");
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_keyop_item* keyop_item_create_node (KEYOP_ITEM keyop_item, u8 *devUnitID, u8 *keyinput_board_id, int index)
{
    node_keyop_item* node = (node_keyop_item*) malloc (sizeof(node_keyop_item));
    memcpy(node->devUnitID, devUnitID, 12);
    memcpy(node->keyinput_board_id, keyinput_board_id, 12);
    node->key_index = index;
    node->keyop_item = keyop_item;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
keyop_item_linked_list keyop_item_create_linked_list ()
{
    keyop_item_linked_list keyop_item_list;

    keyop_item_list.head = NULL;
    keyop_item_list.tail = NULL;
    keyop_item_list.keyop_item_add = &keyop_item_add;
    keyop_item_list.keyop_item_addFirst = &keyop_item_addFirst;
    keyop_item_list.keyop_item_addLast = &keyop_item_addLast;
    keyop_item_list.keyop_item_get = &keyop_item_get;
    keyop_item_list.keyop_item_remove = &keyop_item_remove;
    keyop_item_list.keyop_item_display = &keyop_item_display;
    keyop_item_list.keyop_item_create_node = &keyop_item_create_node;
    keyop_item_list.size = 0;

    return keyop_item_list;
}


/** gw_client_add item to any position
 */
void gw_client_add (gw_client_linked_list* _this, struct sockaddr_in sender, u8 *devUnitID, u8 *devPass, u8 *ip, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->gw_client_addFirst(_this, sender, devUnitID, devPass, ip);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->gw_client_addLast(_this, sender, devUnitID, devPass, ip);
    }
}

/** gw_client_add item to head
 */
void gw_client_addFirst (struct gw_client_linked_list* _this, struct sockaddr_in sender, u8 *devUnitID, u8 *devPass, u8 *ip)
{
    node_gw_client* newNode = _this->gw_client_create_node(sender, devUnitID, devPass, ip);
    node_gw_client* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_gw_client* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void gw_client_addLast (gw_client_linked_list* _this, struct sockaddr_in sender, u8 *devUnitID, u8 *devPass, u8 *ip)
{
    node_gw_client* newNode = _this->gw_client_create_node(sender, devUnitID, devPass, ip);
    node_gw_client* head = _this->head;
    node_gw_client* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_gw_client* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if(head->gw_sender.sin_addr.s_addr == sender.sin_addr.s_addr
               && head->gw_sender.sin_port == sender.sin_port) {
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** display the items in the list
 */
void gw_client_display (gw_client_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("联网模块 has %d items\n", size);
        node_gw_client* node = _this->head;
        for (i = 0; i < size; i++) {
            char rcu_ip[16] = { 0 };
            sprintf(rcu_ip, "%d.%d.%d.%d", node->rcu_ip[0], node->rcu_ip[1], node->rcu_ip[2], node->rcu_ip[3]);

            LOGI("联网模块SCKET IP:%s 端口：%d 模块IP：%s\n", inet_ntoa(node->gw_sender.sin_addr), node->gw_sender.sin_port, rcu_ip);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_gw_client* gw_client_create_node (struct sockaddr_in gw_client, u8 *devUnitID, u8 *devPass, u8 *ip)
{
    node_gw_client* node = (node_gw_client*) malloc (sizeof(node_gw_client));
    memcpy(node->gw_id, devUnitID, 12);
    memcpy(node->gw_pass, devPass, 8);
    memcpy(node->rcu_ip, ip, 4);
    node->gw_sender = gw_client;

    node->prev = NULL;
    node->next = NULL;
    return node;
}

/** create a LinkedList
 */
gw_client_linked_list gw_client_create_linked_list ()
{
    gw_client_linked_list gw_client_list;

    gw_client_list.head = NULL;
    gw_client_list.tail = NULL;
    gw_client_list.gw_client_add = &gw_client_add;
    gw_client_list.gw_client_addFirst = &gw_client_addFirst;
    gw_client_list.gw_client_addLast = &gw_client_addLast;
    gw_client_list.gw_client_display = &gw_client_display;
    gw_client_list.gw_client_create_node = &gw_client_create_node;
    gw_client_list.size = 0;

    return gw_client_list;
}


/** app_client_add item to any position
 */
void app_client_add (app_client_linked_list* _this, struct sockaddr_in sender, u8 *app_ip, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->app_client_addFirst(_this, sender, app_ip);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->app_client_addLast(_this, sender, app_ip);
    }
}

/** app_client_add item to head
 */
void app_client_addFirst (struct app_client_linked_list* _this, struct sockaddr_in sender, u8 *app_ip)
{
    node_app_client* newNode = _this->app_client_create_node(sender, app_ip);
    node_app_client* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_app_client* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void app_client_addLast (app_client_linked_list* _this, struct sockaddr_in sender, u8 *app_ip)
{
    node_app_client* newNode = _this->app_client_create_node(sender, app_ip);
    node_app_client* head = _this->head;
    node_app_client* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_app_client* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if(head->app_sender.sin_addr.s_addr == sender.sin_addr.s_addr) {
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** display the items in the list
 */
void app_client_display (app_client_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("APP模块 has %d items\n", size);
        node_app_client* node = _this->head;
        for (i = 0; i < size; i++) {

            LOGI("APP模块IP:%s 端口：%d\n", inet_ntoa(node->app_sender.sin_addr), node->app_sender.sin_port);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_app_client* app_client_create_node (struct sockaddr_in app_client, u8 *app_ip)
{
    node_app_client* node = (node_app_client*) malloc (sizeof(node_app_client));

    node->app_sender = app_client;
    memcpy(node->app_ip, app_ip, 4);

    node->prev = NULL;
    node->next = NULL;
    return node;
}

/** create a LinkedList
 */
app_client_linked_list app_client_create_linked_list ()
{
    app_client_linked_list app_client_list;

    app_client_list.head = NULL;
    app_client_list.tail = NULL;
    app_client_list.app_client_add = &app_client_add;
    app_client_list.app_client_addFirst = &app_client_addFirst;
    app_client_list.app_client_addLast = &app_client_addLast;
    app_client_list.app_client_display = &app_client_display;
    app_client_list.app_client_create_node = &app_client_create_node;
    app_client_list.size = 0;

    return app_client_list;
}


/** udp_msg_queue_add item to any position
 */
void udp_msg_queue_add (udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd, int id, int flag, int position)
{
    // index out of list size
    if (position > _this->size) {
        LOGI("LinkedList#ware_add: Index out of bound");
        system("PAUSE");
        exit(0);
    }
    // ware_add to head
    if (position == 0) {
        _this->udp_msg_queue_addFirst(_this, devUnitID, cmd, id, flag);
    } else if (position == _this->size) {
        // ware_add to tail
        _this->udp_msg_queue_addLast(_this, devUnitID, cmd, id, flag);
    }
}

/** udp_msg_queue_add item to head
 */
void udp_msg_queue_addFirst (struct udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd, int id, int flag)
{
    node_udp_msg_queue* newNode = _this->udp_msg_queue_create_node(devUnitID, cmd, id, flag);
    node_udp_msg_queue* head = _this->head;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_udp_msg_queue* last = _this->tail;
        if (last == NULL) // only head node
            last = head;
        newNode->next = head;
        head->prev = newNode;
        _this->head = newNode;
        _this->tail = last;
    }

    _this->size++;
}

/** ware_add item to tail
 */
void udp_msg_queue_addLast (udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd, int id, int flag)
{
    node_udp_msg_queue* newNode = _this->udp_msg_queue_create_node(devUnitID, cmd, id, flag);
    node_udp_msg_queue* head = _this->head;
    node_udp_msg_queue* tail = _this->tail;
    // list is empty
    if (head == NULL)
        _this->head = newNode;
    else { // has item(s)
        node_udp_msg_queue* lastNode = tail;
        if (tail == NULL) // only head node
            lastNode = head;

        while(head) {
            if(memcmp(head->devUnitID, devUnitID, 12) == 0
               && head->cmd == cmd
               && head->id == id) {
                head->flag = flag;
                return;
            }
            head = head->next;
        }

        lastNode->next = newNode;
        newNode->prev = lastNode;
        _this->tail = newNode;
        _this->size++;

    }
}

/** get item from specific position
 */
int udp_msg_queue_get (udp_msg_queue_linked_list* _this, int flag)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#get: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_udp_msg_queue* node = _this->head;
    int i = 0;
    // loop until position
    while (node && node->flag == flag) {
        node = node->next;
        i++;
    }

    return i;

}


/** get item and remove it from any position
 */
void udp_msg_queue_remove (udp_msg_queue_linked_list* _this, u8 *devUnitID, int cmd)
{
    // list is empty
    if (_this->size == 0) {
        LOGI("LinkedList#_remove: The list is empty.");
        system("PAUSE");
        exit(0);
    }
    node_udp_msg_queue* node = _this->head;
    node_udp_msg_queue* prev;
    node_udp_msg_queue* next;
    int i = 0;

    // loop until position
    while (memcpy(node->devUnitID, devUnitID, 12) != 0 || node->cmd != cmd) {
        node = node->next;
        i++;
    }

    // remove node from list
    prev = node->prev;
    next = node->next;
    prev->next = next;
    next->prev = prev;
    free(node);
    _this->size--;

}
/** display the items in the list
 */
void udp_msg_queue_display (udp_msg_queue_linked_list* _this)
{
    int i, size = _this->size;
    if (size == 0)
        LOGI("no item\n\n");
    else {
        LOGI("消息队列 has %d items\n", size);
        node_udp_msg_queue* node = _this->head;
        for (i = 0; i < size; i++) {
            LOGI("消息id：%d\n", node->cmd);
            node = node->next;
        }
        LOGI("\n\n");
    }
}

/** create a Node
 */
node_udp_msg_queue* udp_msg_queue_create_node (u8 *uid, int cmd, int id, int flag)
{
    node_udp_msg_queue* node = (node_udp_msg_queue*) malloc (sizeof(node_udp_msg_queue));

    memcpy(node->devUnitID, uid, 12);
    node->cmd = cmd;
    node->id = id;
    node->flag = flag;
    node->prev = NULL;
    node->next = NULL;
    return node;
}


/** create a LinkedList
 */
udp_msg_queue_linked_list udp_msg_queue_create_linked_list ()
{
    udp_msg_queue_linked_list udp_msg_queue_list;

    udp_msg_queue_list.head = NULL;
    udp_msg_queue_list.tail = NULL;
    udp_msg_queue_list.udp_msg_queue_add = &udp_msg_queue_add;
    udp_msg_queue_list.udp_msg_queue_addFirst = &udp_msg_queue_addFirst;
    udp_msg_queue_list.udp_msg_queue_addLast = &udp_msg_queue_addLast;
    udp_msg_queue_list.udp_msg_queue_get = &udp_msg_queue_get;
    udp_msg_queue_list.udp_msg_queue_remove = &udp_msg_queue_remove;
    udp_msg_queue_list.udp_msg_queue_display = &udp_msg_queue_display;
    udp_msg_queue_list.udp_msg_queue_create_node = &udp_msg_queue_create_node;
    udp_msg_queue_list.size = 0;

    return udp_msg_queue_list;
}