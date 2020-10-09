class Tape {
    var head: TapeNode? = null
    var tail: TapeNode? = null
    var length = 0

    inner class TapeNode (var element: Int?) {
        var previous: TapeNode? = null
        var next: TapeNode? = null
    }

    fun add(element: Int?) {
        var last = head
        val newNode = TapeNode(element)

        newNode.next = null

        // If the tape is empty, set the new node as the head
       if (last == null) {
           newNode.previous = null
           head = newNode

           this.length++
           return;
       }

       while (last?.next != null) {
           last = last.next
       }

       last?.next = newNode
       newNode.previous = last
       tail = newNode

       this.length++
    }

    fun add(index: Int, element: Int?) {
        if (index > this.length || index < 0) return
        if (index == 0) addFirst(element)
        if (index == length) add(element)

        val newNode = TapeNode(element)
        var prev = this.head
        var curr = 0

        while (curr < index - 1) {
            prev = prev?.next
            curr++
        }

        val nextNode = prev?.next
        newNode.next = nextNode
        nextNode?.previous = newNode
        prev?.next = newNode
        newNode.previous = prev

        this.length++
    }

    fun addFirst(element: Int?) {
        val first = this.head
        val newNode = TapeNode(element)

        newNode.next = this.head
        head = newNode

        if (first == null) {
            tail = newNode
        } else {
            first.previous = newNode
        }

        this.length++
    }

    fun clear() {
        this.head = null
        this.tail = null
        this.length = 0
    }

    fun get(index: Int): Int? {
        if (index >= this.length || index < 0) return null
        var curr = 0
        var last = this.head
        while (curr < index){
            last = last?.next
            curr++
        }
        return last!!.element
    }

    fun getFirst(): Int? {
        return this.head?.element
    }

    fun getLast(): Int? {
        return this.tail?.element
    }

    fun set(index: Int, value: Int) {
        if (index >= this.length || index < 0) return

        var curr = 0
        var last = this.head

        while (curr < index) {
            last = last?.next
            curr++
        }

        last!!.element = value
    }
}

class Interpreter {

    val tape = Tape()
    
    var currentTapeIndex = 0
    var writeFlag = false
    

    fun sanitizeInput (commands: String): String {
        val SANITIZING_REGEXP = "[^><+*]"

        return commands.replace(SANITIZING_REGEXP.toRegex(),"")
    }

    fun handleOperator (operator: Char) {
        when (operator) {
            '>' -> moveNext()
            '<' -> movePrevious()
            '+' -> incrementCell()
            '*' -> toAscii()
        }
    }

    fun moveNext() {
        currentTapeIndex++
		
        if (currentTapeIndex > tape.length-1) {
            tape.add(0)
        }
    }

    fun movePrevious() {
        currentTapeIndex--

        if (currentTapeIndex < 0) {
            tape.addFirst(0)
            currentTapeIndex = 0
        }
    }

    fun incrementCell() {
        var previous = tape.get(currentTapeIndex)
        
        if(previous != null){
        var newValue = previous + 1

        if (newValue > 255) {
            tape.set(currentTapeIndex, 0)
            return
        }

        tape.set(currentTapeIndex, newValue)
        }
    }

    fun toAscii() {
        print(tape.get(currentTapeIndex)?.toChar())
    }

    fun run(commands: String) {
       val cmd = sanitizeInput(commands)
       tape.addFirst(0)

        for (i in 0..cmd.length-1){
            handleOperator(cmd[i])
        }
    }
}
