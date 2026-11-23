public class CourseHeap {
    public Application[] heap;
    public int size;
    private boolean isMaxHeap;

    public CourseHeap(boolean isMaxHeap) {
        this.heap = new Application[16];
        this.size = 0;
        this.isMaxHeap = isMaxHeap;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public Application peek() {
        if (size == 0) return null;
        return heap[1];
    }

    private boolean higherPriority(Application a, Application b) {
        if (isMaxHeap) {
            return a.compare(b);
        } else {
            return b.compare(a);
        }
    }

    private void ensureCapacity() {
        if (size+1>=heap.length) {
            Application[] newHeap=new Application[heap.length*2];
            for (int i=1;i<=size;i++) {
                newHeap[i]=heap[i];
            }
            heap=newHeap;
        }
    }

    private void swap(int i,int j) {
        Application temp=heap[i];
        heap[i]=heap[j];
        heap[j]=temp;
        heap[i].heapIndex=i;
        heap[j].heapIndex=j;
    }

    private void percolateUp(int idx) {
        while (idx>1) {
            int parent=idx/2;
            if (higherPriority(heap[idx],heap[parent])) {
                swap(idx,parent);
                idx=parent;
            } else {
                break;
            }
        }
    }

    private void percolateDown(int idx) {
        while (2*idx<=size) {
            int left=2*idx;
            int right=left+1;
            int best=left;
            if (right<=size&&higherPriority(heap[right],heap[left])) {
                best=right;
            }
            if (higherPriority(heap[best],heap[idx])) {
                swap(idx,best);
                idx=best;
            } else {
                break;
            }
        }
    }

    public void add(Application app) {
        ensureCapacity();
        size++;
        heap[size] = app;
        app.heapIndex = size;
        percolateUp(size);
    }

    private Application removeAt(int index) {
        Application removed=heap[index];
        Application last=heap[size];
        heap[size]=null;
        size--;
        if (index<=size) {
            heap[index]=last;
            last.heapIndex=index;
            percolateUp(index);
            percolateDown(index);
        }
        removed.heapIndex=-1;
        return removed;
    }
    public void remove(Application app) {
        int idx=app.heapIndex;
        if (idx>=1&&idx<=size&&heap[idx]==app) {
            removeAt(idx);
        }
    }

    public Application poll() {
        if (size == 0) return null;
        return removeAt(1);
    }

    private void quickSort(Application[] arr, int left, int right){
        if (left >= right) return;
        Application pivot = arr[(left + right) / 2];
        int i = left;
        int j = right;
        while (i <= j) {
            while (arr[i].compare(pivot)) i++;
            while (pivot.compare(arr[j])) j--;
            if (i <= j) {
                Application temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }
        if (left < j) quickSort(arr, left, j);
        if (i < right) quickSort(arr, i, right);
    }

    public Application[] getSortedArray() {
        Application[] arr = new Application[size];
        for (int i = 0; i < size; i++) {
            arr[i] = heap[i + 1];
        }
        if (size > 1) {
            quickSort(arr, 0, size - 1);
        }
        return arr;
    }

}
