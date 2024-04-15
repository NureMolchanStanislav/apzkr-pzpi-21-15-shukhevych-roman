using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class CollectionsRepository(MongoDbContext db)
    : BaseRepository<Collection>(db, "Collections"), ICollectionsRepository;