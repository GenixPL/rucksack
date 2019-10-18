import models.Block
import untils.Rucksack
import java.awt.*
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.BoxLayout
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.math.roundToInt


//TODO: GUI
var filepath =
    "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/data -W.txt"
//const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"

//TODO: GUI
const val outputPath =
    "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/result-img.jpg"
//const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

//TODO: GUI print whose 'printlns' that are here and inside 'Rucksack.kt', because they are somehow
// important, and don't cause much of a delay

var maxThreads = 10
var maxTime = 120L // seconds
var imgMultiplier = 10

var textMaxThreads = JTextField("10")
var textMaxTime = JTextField("3600")
var textImgMultiplier = JTextField("10")
val panelImages = JPanel() //JPanel which displays loaded images
val panelBlocks = JPanel()
fun main() {
    val boxlayout = BoxLayout(panelBlocks, BoxLayout.Y_AXIS)
    panelBlocks.setLayout(boxlayout);
    val frame = createJFrame()
}

fun createJFrame(): JFrame {
    val frame = JFrame("Rucksack algorithm")
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.extendedState = JFrame.MAXIMIZED_BOTH

    val panel1 = JPanel()
    val boxlayout = BoxLayout(panel1, BoxLayout.Y_AXIS)
    panel1.layout = boxlayout
    val lbl = JLabel()
    val temp = ImageIcon()
    frame.contentPane.add(panelImages, BorderLayout.CENTER)

    lbl.icon = temp
    panel1.add(lbl)

//    val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel1, panelBlocks);
    frame.contentPane.add(panelBlocks, BorderLayout.WEST)
    val box = JPanel(FlowLayout()) //JPanel containing all buttons
    box.preferredSize = Dimension(125, 1700)
//    frame.add(box)
    frame.contentPane.add(box, BorderLayout.EAST)

    val label_1 = JLabel("Options")
    box.add(label_1)
    val panelImages = JPanel() //JPanel which displays loaded images

    val labelMaxThreads = JLabel("maxThreads")
    val labelMaxTime = JLabel("maxTime")
    val labelImgMultiplier = JLabel("imgMultiplier")

    val canvas = Canvas()
    val bufferedImage = BufferedImage(1725, 995, BufferedImage.TYPE_INT_ARGB)
    val graphics2D = bufferedImage.createGraphics()
    graphics2D.background = Color(255, 255, 255)
    val icon = ImageIcon(bufferedImage)
    val label = JLabel(icon)
    label.updateUI()

    canvas.setSize(1725, 995)
    canvas.background = Color(255, 255, 255)

    panelImages.add(label)

    val textFields = JPanel()
    textFields.layout = BoxLayout(textFields, BoxLayout.Y_AXIS)

    textFields.add(labelMaxThreads)

    textMaxThreads.preferredSize = Dimension(100, 20)
    textFields.add(textMaxThreads)

    textFields.add(labelMaxTime)

    textMaxTime.preferredSize = Dimension(100, 20)
    textFields.add(textMaxTime)

    textFields.add(labelImgMultiplier)

    textImgMultiplier.preferredSize = Dimension(100, 20)
    textFields.add(textImgMultiplier)

    box.add(textFields)

    val btnSelectDirectory = JButton("Choose file")
    btnSelectDirectory.addActionListener {
        val label = JLabel()
        panelImages.add(label)
        val file = JFileChooser()
        file.currentDirectory = File("./src/main/kotlin")

        val filter = FileNameExtensionFilter("*.Text", "txt")
        file.addChoosableFileFilter(filter)
        val result = file.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {
            val selectedFile = file.selectedFile
            filepath = selectedFile.toString()
        } else if (result == JFileChooser.CANCEL_OPTION) {
            println("No File Selected")
        }
    }
    btnSelectDirectory.preferredSize = Dimension(125, 25)
    box.add(btnSelectDirectory)

    val btnStartRucksack = JButton("Start")
    btnStartRucksack.addActionListener {
        println("filePath: " + filepath)

        maxThreads = textMaxThreads.text.toInt()
        maxTime = textMaxTime.text.toLong()
        imgMultiplier = textImgMultiplier.text.toInt()

        println("maxThread: " + maxThreads)
        println("maxTime: " + maxTime)
        println("imgMultiplier: " + imgMultiplier)
        val res = Rucksack(filepath).findBest()// ?: return

        println()
        println("Best value: ${res?.bestValue}")
        println("Best subset: ")
        res?.bestSubset?.blocks?.forEach {
            println(it)
        }

        println("Generating image...")
        val output = File(outputPath)
        // saving result to 'b' forces awaiting for this generating to end
        val b = ImageIO.write(res?.bufferedImage, "jpg", output)
        displayImage(outputPath, frame)
        displayBlocks()
        println("Generating done")

    }
    btnStartRucksack.preferredSize = Dimension(125, 25)
    box.add(btnStartRucksack)
    displayBlocks()
    frame.isVisible = true
    return frame
}

fun displayImage(outputPath: String, frame: JFrame) {
    val file = File(outputPath)
    val image = ImageIO.read(file)
    val icon = ImageIcon(image)
    val lbl = JLabel()

    panelImages.removeAll()
    panelImages.updateUI()
    panelImages.add(lbl)
    lbl.icon = icon
    frame.isVisible = true
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
    var maxV = 0
    blocks.forEach {
        if(maxV < it.value)
            maxV = it.value
    }
    panelBlocks.removeAll()

    blocks.forEach {
        val lbl = JLabel()

        panelBlocks.add(lbl)
        lbl.icon = createImageIcon(it.width, it.height, it.value.toFloat() / maxV)
    }
        panelBlocks.updateUI()

}


fun createImageIcon(w: Int, h: Int, v: Float) : ImageIcon{
    val bufferedImage = BufferedImage(w * imgMultiplier, h * imgMultiplier, BufferedImage.TYPE_INT_RGB)
    val graphics2D = bufferedImage.createGraphics()
    graphics2D.paint = Color( (255 * v).roundToInt(), 0,0)
    graphics2D.fillRect(0, 0, w * imgMultiplier, h * imgMultiplier)

    return ImageIcon(bufferedImage)
}
