using System.Globalization;
using System.Text;
using API.Controllers.Base;
using CsvHelper;
using CsvHelper.Configuration;
using Microsoft.AspNetCore.Mvc;
using MongoDB.Bson;
using MongoDB.Driver;
using Persistence.Database;

namespace API.Controllers;

[Route("api/export")]
public class ExportController : BaseController
{
    private readonly MongoDbContext _context;
        
    public  ExportController(MongoDbContext context)
    {
        _context = context;
    }
    
    [HttpGet("exportCVS/{collectionName}")]
    public async Task<IActionResult> ExportToCsv(string collectionName)
    {
        var collection = _context.Db.GetCollection<BsonDocument>(collectionName);
        var data = await collection.Find(new BsonDocument()).ToListAsync();

        // Create CSV
        var configuration = new CsvConfiguration(CultureInfo.InvariantCulture)
        {
            Delimiter = ",",
            Encoding = Encoding.UTF8
        };

        using (var memoryStream = new MemoryStream())
        using (var writer = new StreamWriter(memoryStream, Encoding.UTF8))
        using (var csv = new CsvWriter(writer, configuration))
        {
            foreach (var document in data)
            {
                csv.WriteField(document.ToJson());
                csv.NextRecord();
            }

            writer.Flush();
            memoryStream.Position = 0;

            return new FileContentResult(memoryStream.ToArray(), "text/csv")
            {
                FileDownloadName = $"{collectionName}.csv"
            };
        }
    }
}