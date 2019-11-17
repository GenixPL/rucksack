import models.Block
import untils.Rucksack
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.BoxLayout
import javax.swing.border.Border
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.roundToInt

var filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"
const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

var maxThreads = 5
var maxTime = 600L // seconds
var imgMultiplier = 10

val panelImage = JPanel() //JPanel which displays generated image
val panelBlocks = JPanel() //JPanel which displays blocks one by one

val textMaxThreads = JTextField(maxThreads.toString())
val textMaxTime = JTextField(maxTime.toString())
val textImgMultiplier = JTextField(imgMultiplier.toString())

fun main() {
    runProgram()
}

fun runProgram(){
    val frame = initFrameProperties()

    addFrameComponents(frame)

    frame.isVisible = true
}

fun initFrameProperties(): JFrame {
    val frame = JFrame("Rucksack algorithm")

    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.extendedState = JFrame.MAXIMIZED_BOTH
    frame.minimumSize = Dimension(400, 1000)
    return frame
}

fun addFrameComponents(frame: JFrame){
    panelBlocks.preferredSize = Dimension(700, 3000)
    val scrollPane = JScrollPane(panelBlocks, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
    frame.contentPane.add(scrollPane, BorderLayout.WEST)

    panelImage.preferredSize = Dimension(3000, 3000)
    val scrollPaneImage = JScrollPane(panelImage, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED)
    frame.contentPane.add(scrollPaneImage, BorderLayout.CENTER)

    val optionsPanel = createOptionsPanel(frame)
    frame.contentPane.add(optionsPanel, BorderLayout.EAST)

    val boxLayout = BoxLayout(panelBlocks, BoxLayout.Y_AXIS)
    panelBlocks.layout = boxLayout

    val boxLayoutImage = BoxLayout(panelImage, BoxLayout.Y_AXIS)
    panelImage.layout = boxLayoutImage
}

fun createOptionsPanel(frame: JFrame): JPanel {
    val optionsPanel = JPanel(FlowLayout()) //JPanel with all text fields and buttons
    optionsPanel.preferredSize = Dimension(125, 1000)

    val optionsLabel = JLabel("Options")
    optionsPanel.add(optionsLabel)

    val textFields = JPanel()
    textFields.layout = BoxLayout(textFields, BoxLayout.Y_AXIS)

    val labelMaxThreads = JLabel("maxThreads")
    textFields.add(labelMaxThreads)

    textMaxThreads.preferredSize = Dimension(100, 20)
    textFields.add(textMaxThreads)

    val labelMaxTime = JLabel("maxTime")
    textFields.add(labelMaxTime)

    textMaxTime.preferredSize = Dimension(100, 20)
    textFields.add(textMaxTime)

    val labelImgMultiplier = JLabel("imgMultiplier")
    textFields.add(labelImgMultiplier)

    textImgMultiplier.preferredSize = Dimension(100, 20)
    textFields.add(textImgMultiplier)

    optionsPanel.add(textFields)

    addButtons(optionsPanel, frame)

    return optionsPanel
}

fun addButtons(optionsPanel: JPanel, frame: JFrame) {
    val btnSelectDirectory = JButton("Choose file")
    btnSelectDirectory.addActionListener {
        val file = JFileChooser()
        file.currentDirectory = File("./src/main/kotlin")
        getValuesFromTextFields()

        val filter = FileNameExtensionFilter("*.Text", "txt")
        file.addChoosableFileFilter(filter)
        val result = file.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = file.selectedFile
            filepath = selectedFile.toString()
        } else if (result == JFileChooser.CANCEL_OPTION) {
            println("No File Selected")
        }
        displayBlocks()
        panelImage.removeAll()
        panelImage.updateUI()
    }
    btnSelectDirectory.preferredSize = Dimension(125, 25)
    optionsPanel.add(btnSelectDirectory)

    val btnStartRucksack = JButton("Start")
    btnStartRucksack.addActionListener {
        getValuesFromTextFields()

        val res = Rucksack(filepath).findBest()// ?: return

        println("\nBest value: ${res?.bestValue}")
        println("Best subset: ")
        res?.bestSubset?.blocks?.forEach {
            println(it)
        }

        println("Generating image...")
        val output = File(outputPath)

        // saving result to 'b' forces awaiting for this generating to end
        val b = ImageIO.write(res?.bufferedImage, "jpg", output)

        displayImage(outputPath)
        println("Generating done")
    }
    btnStartRucksack.preferredSize = Dimension(125, 25)
    optionsPanel.add(btnStartRucksack)
}

fun getValuesFromTextFields(){
    maxThreads = textMaxThreads.text.toInt()
    maxTime = textMaxTime.text.toLong()
    imgMultiplier = textImgMultiplier.text.toInt()
}

fun displayImage(outputPath: String) {
    val file = File(outputPath)
    val image = ImageIO.read(file)

    val lbl = JLabel()
    lbl.icon = ImageIcon(image)

    panelImage.removeAll()
    panelImage.updateUI()
    panelImage.add(lbl)
}

fun displayBlocks() {
    val file = File(filepath)
    val scanner = Scanner(file)

    val blocks = mutableListOf<Block>()
    val wBoard = scanner.nextInt()
    val hBoard = scanner.nextInt()

    val n = scanner.nextInt()
    for (i in 0 until n) {
        val w = scanner.nextInt()
        val h = scanner.nextInt()
        val v = scanner.nextInt()

        blocks.add(Block(w, h, v))
    }

    var maxValue = 0

    //this loop is used to find maximum value
    blocks.forEach {
        if (maxValue < it.value)
            maxValue = it.value
    }

    panelBlocks.removeAll()

    //this loop is used to display blocks one by one
    blocks.forEach {
        var description = "w: " + it.width.toString() + " h: " + it.height.toString() + " v: " + it.value.toString()
        panelBlocks.add(JLabel(description))

        val lbl = JLabel()
        lbl.icon = createImageIcon(it.width, it.height, it.value.toFloat() / maxValue)
        panelBlocks.add(lbl)
    }
    panelBlocks.updateUI()

}

fun createImageIcon(w: Int, h: Int, v: Float): ImageIcon {
    val bufferedImage = BufferedImage(w * imgMultiplier, h * imgMultiplier, BufferedImage.TYPE_INT_RGB)
    val graphics2D = bufferedImage.createGraphics()
    graphics2D.paint = Color((255 * v).roundToInt(), 0, 0)
    graphics2D.fillRect(0, 0, w * imgMultiplier, h * imgMultiplier)

    return ImageIcon(bufferedImage)
}
