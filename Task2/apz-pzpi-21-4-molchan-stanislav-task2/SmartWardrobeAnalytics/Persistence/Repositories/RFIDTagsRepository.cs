using Application.IRepositories;
using Application.Models.UpdateDtos;
using Domain.Entities;
using MongoDB.Bson;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class RFIDTagsRepository(MongoDbContext db) : BaseRepository<RFIDTag>(db, "RFIDTags"), IRFIDTagsRepository
{
    public async Task<bool> GetStatus(string sensorId, CancellationToken cancellationToken)
    {
        var filter = MongoDB.Driver.Builders<RFIDTag>.Filter.Eq(x => x.Id, ObjectId.Parse(sensorId));
        var sensor = await _collection.Find(filter).FirstOrDefaultAsync();

        if (sensor != null)
        {
            return sensor.Status;
        }
        
        return false;
    }
    
    public async Task<bool> UpdateStatus(RFIDTagStatusUpdate statusUpdate, CancellationToken cancellationToken)
    {
        var filter = MongoDB.Driver.Builders<RFIDTag>.Filter.Eq(x => x.Id, ObjectId.Parse(statusUpdate.Id));
        var update = MongoDB.Driver.Builders<RFIDTag>.Update
            .Set(x => x.Status, statusUpdate.Status)
            .Set(x => x.LastModifiedDateUtc, DateTime.UtcNow);

        var result = await _collection.UpdateOneAsync(filter, update);
        
        return true;
    }
}