package com.bigbrother.fileservice.service;

import com.bigbrother.fileservice.consts.SysConst;
import com.bigbrother.fileservice.dto.FileInfo;
import com.bigbrother.fileservice.exception.FileServiceRuntimeException;
import com.bigbrother.fileservice.utils.SysUtil;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.stereotype.Service;

/**
 * @author Administrator
 */
@Slf4j
@Service
public class LuceneService {


    /**
     *
     * @param userName String
     * @return Directory
     * @throws IOException IOException
     */
    private Directory getDirectory(String userName) throws IOException {
        String fileName = SysUtil.getLuceneRoot().concat(userName);
        File file = new File(fileName);
        if (!file.exists() && !file.mkdirs()) {
            throw new FileServiceRuntimeException("文件夹创建失败");
        }
        return FSDirectory.open(Paths.get(fileName));
    }

    public boolean create(FileInfo fileInfo) {
        try (Directory directory = getDirectory(fileInfo.getUserName());
             IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(getAnalyzer(fileInfo.getTags())))) {
            indexWriter.addDocument(buildDocument(fileInfo));
            indexWriter.commit();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public boolean update(FileInfo fileInfo) {
        try (Directory directory = getDirectory(fileInfo.getUserName());
             IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(getAnalyzer(fileInfo.getTags())))) {
            indexWriter.updateDocument(new Term(SysConst.KEY_ID, fileInfo.getId()), buildDocument(fileInfo));
            indexWriter.commit();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    private Document buildDocument(FileInfo fileInfo){
        Document document = new Document();
        document.add(new StringField(SysConst.KEY_ID, fileInfo.getFileName(), Field.Store.YES));
        document.add(new StringField(SysConst.KEY_TAGS, fileInfo.getTags(), Field.Store.YES));
        document.add(new StringField(SysConst.KEY_FILENAME, fileInfo.getTags(), Field.Store.NO));
        return document;
    }

    public boolean delete(FileInfo fileInfo) {
        try (Directory directory = getDirectory(fileInfo.getUserName());
             IndexWriter indexWriter = new IndexWriter(directory, new IndexWriterConfig(new StandardAnalyzer()))) {
            indexWriter.deleteDocuments(new Term(SysConst.KEY_ID, fileInfo.getId()));
            indexWriter.commit();
        } catch (IOException e) {
            return false;
        }
        return true;
    }


    public boolean rebuild() {
        //TODO 重建索引
        return true;
    }

    public List<FileInfo> search(FileInfo fileInfo) {
        List<FileInfo> list = new ArrayList<>();
        try (Directory directory = getDirectory(fileInfo.getUserName());
             DirectoryReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = getAnalyzer(fileInfo.getTags());
            Query query = new QueryParser(SysConst.KEY_TAGS, analyzer).parse(fileInfo.getTags());
            QueryScorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(new SimpleHTMLFormatter(), scorer);
            highlighter.setTextFragmenter(new SimpleSpanFragmenter(scorer));
            ScoreDoc[] scoreDocs = searcher.search(query, 50).scoreDocs;
            for (ScoreDoc scoreDoc : scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                String tags = doc.get(SysConst.KEY_TAGS);
                if (tags != null) {
                    TokenStream tokenStream = analyzer.tokenStream("tags", new StringReader(tags));
                    list.add(new FileInfo().setId(doc.get(SysConst.KEY_ID)).setFileName(doc.get(SysConst.KEY_FILENAME))
                            .setTags(highlighter.getBestFragment(tokenStream, tags)));
                }
            }
        } catch (IOException | ParseException | InvalidTokenOffsetsException ex) {
            throw new FileServiceRuntimeException("搜索失败");
        }
        return list;
    }

    private Analyzer getAnalyzer(String str) {
        return SysUtil.containChinese(str) ? new SmartChineseAnalyzer() : new StandardAnalyzer();
    }

}
