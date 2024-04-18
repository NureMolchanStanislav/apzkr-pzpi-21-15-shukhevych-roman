using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;
using MongoDB.Driver;

namespace Persistence.Repositories;

public class OffersRepository(MongoDbContext db) : BaseRepository<Offer>(db, "Offers"), IOffersRepository
{
    public async Task<Offer> UpdateAsync(Offer offer, CancellationToken cancellationToken)
    {
        var updateDefinition = MongoDB.Driver.Builders<Offer>.Update
            .Set(c=> c.Discount, offer.Discount)
            .Set(c => c.LastModifiedDateUtc, offer.LastModifiedDateUtc)
            .Set(c => c.LastModifiedById, offer.LastModifiedById);

        var options = new MongoDB.Driver.FindOneAndUpdateOptions<Offer>
        {
            ReturnDocument = ReturnDocument.After
        };

        return await this._collection.FindOneAndUpdateAsync(MongoDB.Driver.Builders<Offer>.Filter.Eq(u => u.Id, offer.Id), 
            updateDefinition, 
            options, 
            cancellationToken);
    }
}