package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.StringFormat
import ru.astrainteractive.astralibs.util.YamlStringFormat
import java.net.InetAddress
import java.net.InetSocketAddress
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class InetSocketAddressSerializerTest {

    private val format: StringFormat = YamlStringFormat()

    @Test
    fun GIVEN_socket_address_WHEN_encode_decode_THEN_round_trips() {
        val socketAddress = InetSocketAddress(InetAddress.getByName("10.0.0.5"), 25565)

        val encoded = format.encodeToString(InetSocketAddressSerializer, socketAddress)
        val decoded = format.decodeFromString(InetSocketAddressSerializer, encoded)

        assertEquals(socketAddress, decoded)
    }

    @Test
    fun GIVEN_socket_address_WHEN_decoded_THEN_preserves_address_and_port() {
        val socketAddress = InetSocketAddress(InetAddress.getByName("127.0.0.1"), 8080)

        val encoded = format.encodeToString(InetSocketAddressSerializer, socketAddress)
        val decoded = format.decodeFromString(InetSocketAddressSerializer, encoded)

        assertEquals(8080, decoded.port)
        assertEquals(InetAddress.getByName("127.0.0.1"), decoded.address)
    }

    @Test
    fun GIVEN_boundary_ports_WHEN_encode_decode_THEN_round_trips() {
        listOf(0, 65535).forEach { port ->
            val socketAddress = InetSocketAddress(InetAddress.getByName("127.0.0.1"), port)

            val encoded = format.encodeToString(InetSocketAddressSerializer, socketAddress)
            val decoded = format.decodeFromString(InetSocketAddressSerializer, encoded)

            assertEquals(socketAddress, decoded)
        }
    }

    @Test
    fun GIVEN_ipv6_socket_address_WHEN_encode_decode_THEN_round_trips() {
        val socketAddress = InetSocketAddress(InetAddress.getByName("::1"), 25565)

        val encoded = format.encodeToString(InetSocketAddressSerializer, socketAddress)
        val decoded = format.decodeFromString(InetSocketAddressSerializer, encoded)

        assertEquals(socketAddress, decoded)
    }
}
