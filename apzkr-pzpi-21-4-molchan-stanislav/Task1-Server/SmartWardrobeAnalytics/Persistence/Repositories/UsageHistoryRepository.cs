using Application.IRepositories;
using Domain.Entities;
using MongoDB.Driver;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class UsageHistoryRepository(MongoDbContext db) : BaseRepository<UsageHistory>(db, "UsageHistory"), IUsageHistoryRepository
{
    public async Task<List<UsageHistory>> GetAllAsync(CancellationToken cancellationToken = default)
    {
        return await _collection.Find(_ => true).ToListAsync(cancellationToken);
    }
}