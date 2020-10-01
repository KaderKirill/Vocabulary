public class LineList<E> { // односвязный список

    private Node<E> firstElement;
    private Node<E> lastElement;
    private int size = 0;

    public LineList() {
        lastElement = new Node<E>(null, null);
        firstElement = new Node<E>(null, lastElement);
    }

    private static class Node<E> {
        E element;
        Node<E> next;

        Node(E element, Node<E> next) {
            this.element = element;
            this.next = next;
        }

        private E getElement() {
            return element;
        }

        private void setElement(E element) {
            this.element = element;
        }

        private Node<E> getNext() {
            return next;
        }

        private void setNext(Node<E> next) {
            this.next = next;
        }
    }


    public void addFirst(E element) {
        Node<E> first = firstElement;
        first.setElement(element);
        firstElement = new Node<E>(null, first);
        size++;

    }

    public void addLast(E e) {
        Node<E> prev = lastElement;
        prev.element = e;
        lastElement = new Node<E>(null, prev);
        prev.next = lastElement;
        size++;
    }

    public void addByIndex(E e, int index) {
        if (index == 0) {
            addFirst(e);
        } else if (index == size()) {
            addLast(e);
        } else {
            Node<E> tm_el = firstElement.getNext();
            for (int i = 1; i < index - 1; i++) {
                tm_el = tm_el.getNext();
            }
            tm_el.getNext().setElement(e);
            tm_el.setNext(tm_el.getNext());
            tm_el.getNext().setNext(tm_el.getNext().getNext());
        }
    }

    public void removeFirst() {
        Node<E> tm_el = firstElement.getNext();
        firstElement.setNext(tm_el.getNext());
        tm_el.setElement(null);
        tm_el.setNext(null);
        size--;
    }

    public int size() {
        return size;
    }

    public E getElByIdx(int indx) {  // метод для получения элемента по индексу
        Node<E> e;
        while (indx > size) {
            indx -= size;
        }
        e = firstElement.getNext();
        for (int j = 0; j < indx; j++) {
            e = e.getNext();
        }

        return e.getElement();
    }

    public void removeElByIdx(int indx) {
        if (size == 0) {
            return;
        }
        while (indx >= size) {
            indx -= size;
        }
        if (indx == 0) {
            removeFirst();
        } else {
            Node<E> timeEl = firstElement.getNext();
            for (int i = 0; i < indx - 1; i++) {
                timeEl = timeEl.getNext();
            }
            Node<E> timeEl2 = timeEl.getNext();
            timeEl.setNext(timeEl2.getNext());
            timeEl2.setElement(null);
            timeEl2.setNext(null);
            size--;
        }
    }

    public void clear() {
        while (size != 0) {
            removeFirst();
        }
    }
}
