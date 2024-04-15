using Application.IRepositories;
using Domain.Entities;
using Persistence.Database;
using Persistence.Repositories.Base;

namespace Persistence.Repositories;

public class RefreshTokensRepository(MongoDbContext db)
    : BaseRepository<RefreshToken>(db, "RefreshTokens"), IRefreshTokensRepository;