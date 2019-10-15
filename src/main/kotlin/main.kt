import untils.Rucksack
import java.awt.*
import java.awt.image.BufferedImage
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import javax.imageio.ImageIO
import javax.swing.*
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.reflect.typeOf


//TODO: GUI
var filepath = ""
   // "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/data.txt"
//const val filepath = "/Users/genix/Projects/rucksack/src/main/kotlin/data.txt"

//TODO: GUI
const val outputPath =
    "C:/Users/klosi/Desktop/Studies/7th SEMESTER/Algoriths and computability/Project/rucksack/src/main/kotlin/result-img.jpg"
//const val outputPath = "/Users/genix/Projects/rucksack/src/main/kotlin/result-img.jpg"

//TODO: GUI print whose 'printlns' that are here and inside 'Rucksack.kt', because they are somehow
// important, and don't cause much of a delay

//TODO: GUI
var maxThreads = 10

//TODO: GUI
var maxTime = 120L // seconds

//TODO: GUI
var imgMultiplier = 10

var textMaxThreads = JTextField("10")
var textMaxTime = JTextField("3600")
var textImgMultiplier = JTextField("10")
var isWaiting = false
val btnSelectdirectory = JButton("Choose image")

fun main() {

    val frame = createJFrame()
    while (filepath.equals("")) {
        Thread.sleep(100)
    }

    println("filePath: " + filepath)

    maxThreads = textMaxThreads.text.toInt()
    maxTime = textMaxTime.text.toLong()
    imgMultiplier = textImgMultiplier.text.toInt()

    println("maxThread: " + maxThreads)
    println("maxTime: " + maxTime)
    println("imgMultiplier: " + imgMultiplier)
    val res = Rucksack(filepath).findBest() ?: return

    println()
    println("Best value: ${res.bestValue}")
    println("Best subset: ")
    res.bestSubset.blocks.forEach {
        println(it)
    }

    println("Generating image...")
    val output = File(outputPath)
    // saving result to 'b' forces awaiting for this generating to end
    val b = ImageIO.write(res.bufferedImage, "jpg", output)
    println("Generating done")
}

fun createJFrame(): JFrame {
    isWaiting = true
    val frame = JFrame("Karol Klosowski - program")
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
    frame.setSize(700, 700)
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH)
    frame.setResizable(true)
    frame.setVisible(true)

    val splitPane = JSplitPane()
    frame.getContentPane().add(splitPane, BorderLayout.EAST)

    val box = JPanel(FlowLayout()) //JPanel containing all buttons
    box.preferredSize = Dimension(125, 1700)
    splitPane.leftComponent = box
    val label_1 = JLabel("Options")
    box.add(label_1)
    val panelImages = JPanel() //JPanel which displays loaded images

    val labelMaxThreads = JLabel("maxThreads")
    val labelMaxTime = JLabel("maxTime")
    val labelImgMultiplier = JLabel("maxTime")


    panelImages.removeAll()
    panelImages.updateUI()
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

    btnSelectdirectory.addActionListener {
        val label: JLabel
        label = JLabel()
        panelImages.add(label)
        val file = JFileChooser()
        file.currentDirectory = File("./src/main/kotlin")
        //filter the files
        val filter = FileNameExtensionFilter("*.Text", "txt")
        file.addChoosableFileFilter(filter)
        val result = file.showSaveDialog(null)

        if (result == JFileChooser.APPROVE_OPTION) {

            val selectedFile = file.selectedFile
            filepath = selectedFile.toString()
            isWaiting = false
        } else if (result == JFileChooser.CANCEL_OPTION) {
            println("No File Select")
        }
    }
    btnSelectdirectory.preferredSize = Dimension(125, 25)
    box.add(btnSelectdirectory)

    frame.isVisible = true
    return frame
}
