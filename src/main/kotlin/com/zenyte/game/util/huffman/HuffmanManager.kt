package com.zenyte.game.util.huffman

import com.zenyte.Game
import com.zenyte.game.util.charset.Cp1252Charset
import com.zenyte.game.util.readUnsignedShortSmart
import com.zenyte.game.util.writeUnsignedShortSmart
import com.zenyte.network.io.RSBuffer
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufAllocator
import io.netty.buffer.Unpooled
import mgi.tools.jagcached.cache.Cache
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Jire
 */
object HuffmanManager {

    private val logger: Logger = LoggerFactory.getLogger(HuffmanManager::class.java)

    @JvmStatic
    lateinit var huffman: HuffmanKotlin
        private set

    @JvmStatic
    @JvmOverloads
    fun load(cache: Cache = Game.getCacheMgi()) = HuffmanKotlin.load(cache).apply {
        huffman = this
    }

    const val MAX_HUFFMAN_STRING_LENGTH = 255/*32767*/
    const val MAX_HUFFMAN_BUFFER_CAPACITY = 10_000

    private val reusableDest: ThreadLocal<ByteBuf> = ThreadLocal.withInitial {
        Unpooled.directBuffer(MAX_HUFFMAN_STRING_LENGTH, MAX_HUFFMAN_STRING_LENGTH)
    }

    @JvmStatic
    @JvmOverloads
    fun RSBuffer.readHuffmanString(
        huffman: HuffmanKotlin = HuffmanManager.huffman
    ): String = try {
        val content = this
        val length = content.readUnsignedShortSmart()
            .coerceAtMost(MAX_HUFFMAN_STRING_LENGTH)

        val dest = reusableDest.get().clear()
        val written = huffman.decompress(content, dest, length)
        dest.toString(0, written, Cp1252Charset)
    } catch (e: Exception) {
        logger.error("Failed to read huffman string", e)
        "Cabbage"
    }

    private fun String.ensureLength(): Int {
        val length = length
        if (length > MAX_HUFFMAN_STRING_LENGTH) throw IllegalArgumentException(
            "String \"$this\" exceeds max huffman length ($MAX_HUFFMAN_STRING_LENGTH)"
        )
        return length
    }

    @JvmStatic
    @JvmOverloads
    fun encodeHuffmanBuf(
        string: String,
        huffman: HuffmanKotlin = HuffmanManager.huffman,
        allocator: ByteBufAllocator = ByteBufAllocator.DEFAULT
    ): ByteBuf {
        val length = string.ensureLength()

        val bufOffset = if (length > 0x7F) 2 else 1
        val bufLength = length + bufOffset

        val buf = allocator.directBuffer(bufLength, MAX_HUFFMAN_BUFFER_CAPACITY)
        encodeHuffman(huffman, buf, string, length)
        return buf
    }

    @JvmStatic
    @JvmOverloads
    fun encodeHuffmanBuf(
        buf: ByteBuf,
        string: String,
        huffman: HuffmanKotlin = HuffmanManager.huffman
    ) {
        val length = string.ensureLength()
        encodeHuffman(huffman, buf, string, length)
    }

    private fun encodeHuffman(huffman: HuffmanKotlin, buf: ByteBuf, string: String, length: Int) {
        buf.writeUnsignedShortSmart(length)

        val started = buf.writerIndex()
        val written = huffman.compress(string, 0, length, buf, started)
        buf.writerIndex(buf.writerIndex() + written)
    }

}
