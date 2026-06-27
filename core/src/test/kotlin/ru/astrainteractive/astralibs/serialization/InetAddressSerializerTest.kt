package ru.astrainteractive.astralibs.serialization

import kotlinx.serialization.StringFormat
import ru.astrainteractive.astralibs.util.YamlStringFormat
import java.net.InetAddress
import kotlin.test.Test
import kotlin.test.assertEquals

@Suppress("TestFunctionName")
class InetAddressSerializerTest {

    private val format: StringFormat = YamlStringFormat()

    @Test
    fun GIVEN_ipv4_address_WHEN_encode_decode_THEN_round_trips() {
        val address = InetAddress.getByName("192.168.1.1")

        val encoded = format.encodeToString(InetAddressSerializer, address)
        val decoded = format.decodeFromString(InetAddressSerializer, encoded)

        assertEquals(address, decoded)
        assertEquals("192.168.1.1", decoded.hostAddress)
    }

    @Test
    fun GIVEN_another_ipv4_address_WHEN_encode_decode_THEN_round_trips() {
        val address = InetAddress.getByName("8.8.8.8")

        val encoded = format.encodeToString(InetAddressSerializer, address)
        val decoded = format.decodeFromString(InetAddressSerializer, encoded)

        assertEquals(address, decoded)
    }

    @Test
    fun GIVEN_ipv6_address_WHEN_encode_decode_THEN_round_trips() {
        val address = InetAddress.getByName("2001:db8:0:0:0:0:0:1")

        val encoded = format.encodeToString(InetAddressSerializer, address)
        val decoded = format.decodeFromString(InetAddressSerializer, encoded)

        assertEquals(address, decoded)
    }

    @Test
    fun GIVEN_wildcard_address_WHEN_encode_decode_THEN_round_trips() {
        val address = InetAddress.getByName("0.0.0.0")

        val encoded = format.encodeToString(InetAddressSerializer, address)
        val decoded = format.decodeFromString(InetAddressSerializer, encoded)

        assertEquals(address, decoded)
    }
}
