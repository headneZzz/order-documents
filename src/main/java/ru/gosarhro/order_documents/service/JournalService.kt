package ru.gosarhro.order_documents.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.gosarhro.order_documents.entity.Journal
import ru.gosarhro.order_documents.model.DocumentsFilter
import ru.gosarhro.order_documents.repository.DigitizedRepository
import ru.gosarhro.order_documents.repository.JournalRepository
import java.io.File
import java.util.*
import kotlin.math.roundToInt

@Service
class JournalService (
    private val journalRepository: JournalRepository,
    private val digitizedRepository: DigitizedRepository
){

    private val log: Logger = LoggerFactory.getLogger(JournalService::class.java)

    fun getDigitDocsFromDb(): List<Journal> {
        return journalRepository.findAll()
    }

    fun getDigitDocsFromDbWithFilter(documentsFilter: DocumentsFilter): List<Journal> {
        return journalRepository.findAllByDigitizationDateBetweenAndExecutor_NameIsLikeIgnoreCase(documentsFilter.dateFrom, documentsFilter.dateTo, documentsFilter.executor)
    }

    fun refresh() {
        val uniqueDocsFromFondsDb: MutableList<String> = ArrayList()
        val journals = journalRepository.findAll()
        val digitizationDocs = digitizedRepository.findAll()
        val docsFromJournalDb: List<String> = journals.map { it.fod!! }.toList()
        digitizationDocs.forEach { digitizationDoc ->
            val names = digitizationDoc.fileName?.split("_".toRegex())?.dropLastWhile { it.isEmpty() }!!.toTypedArray()
            val nameOfDoc = names[0] + '_' + names[1] + '_' + names[2]
            if (!uniqueDocsFromFondsDb.contains(nameOfDoc) && !docsFromJournalDb.contains(nameOfDoc)) {
                uniqueDocsFromFondsDb.add(nameOfDoc)
                var filesCount: Long = 0
                var sizeInBytes: Long = 0
                val docInFonds = File(digitizationDoc.ref!!.substring(1))
                for (file in Objects.requireNonNull(docInFonds.parentFile.listFiles())) {
                    sizeInBytes += file.length()
                    filesCount += 1
                }
                var sizeInMb = sizeInBytes.toDouble() / (1024 * 1024)
                sizeInMb = (sizeInMb * 100.0).roundToInt() / 100.0
                val journal = Journal()
                with(journal) {
                    fod = nameOfDoc
                    executor = digitizationDoc.executor
                    digitizationDate = digitizationDoc.createdDate
                    this.filesCount = filesCount
                    this.sizeInMb = sizeInMb
                }
                journalRepository.save(journal)
            }
        }
    }
}
